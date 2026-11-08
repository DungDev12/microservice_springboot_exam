package com.shop.orderservice.service;

import com.shop.grpc.inventory.*;
import com.shop.grpc.product.OrderItems;
import com.shop.grpc.product.ProductGrpc;
import com.shop.grpc.product.ProductGrpcRequest;
import com.shop.grpc.product.ProductGrpcResponse;
import com.shop.orderservice.client.grpc.InventoryGrpcClient;
import com.shop.orderservice.client.grpc.ProductGrpcClient;
import com.shop.orderservice.common.enums.OrderPaymentStatus;
import com.shop.orderservice.common.enums.OrderStatus;
import com.shop.orderservice.common.enums.PaymentStatus;
import com.shop.orderservice.dto.request.AddOrderRequest;
import com.shop.orderservice.dto.request.CreateOrderRequest;
import com.shop.orderservice.dto.request.PaymentOrderRequest;
import com.shop.orderservice.dto.request.SePayWebhookRequest;
import com.shop.orderservice.dto.response.OrderResponse;
import com.shop.orderservice.dto.response.PaymentResponse;
import com.shop.orderservice.dto.response.SePayPaymentResponse;
import com.shop.orderservice.entity.Order;
import com.shop.orderservice.entity.OrderItem;
import com.shop.orderservice.event.NotificationEventProducer;
import com.shop.orderservice.event.TransferNotificationEvent;
import com.shop.orderservice.exception.InternalServerException;
import com.shop.orderservice.exception.NotFoundException;
import com.shop.orderservice.mapper.OrderMapper;
import com.shop.orderservice.repository.OrderRepository;
import com.shop.orderservice.service.interfaces.IOrderService;
import com.shop.orderservice.utils.OrderCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderCodeGenerator orderCodeGenerator;
    private final ProductGrpcClient productGrpcClient;
    private final InventoryGrpcClient inventoryGrpcClient;
    private final SePayService sePayService;
    private final NotificationEventProducer notificationEventProducer;


    @Transactional
    @Override
    public OrderResponse createOrder(String uuid, CreateOrderRequest request) {
        // Khách hàng đặt đơn tạo đơn hàng
        Order order = orderMapper.toEntity(request);
        order.setCustomerUuid(UUID.fromString(uuid));
        order.setCode(orderCodeGenerator.generate());

        ProductGrpcResponse response = productGrpcClient.getProductById(
                ProductGrpcRequest.newBuilder()
                .addAllOrderItems(
                        request.items().stream()
                                .map(item -> OrderItems.newBuilder()
                                        .setProductId(item.productId())
                                        .setQuantity(item.quantity())
                                        .build())
                                .toList()
        ).build());


        // Thêm sản phẩm mua vào
        Map<Long, ProductGrpc> productMap = response.getProductsList().stream()
                .collect(Collectors.toMap(ProductGrpc::getId, p -> p));

             List<OrderItem> orderItems = request.items().stream()
                .map(item -> {
                    OrderItem i = new OrderItem();
                    i.setProductId(item.productId());
                    i.setProductName(productMap.get(item.productId()).getName());
                    i.setPrice(new BigDecimal(productMap.get(item.productId()).getPrice()));
                    i.setQuantity(Long.valueOf(item.quantity()));
                    i.setDiscount(productMap.get(item.productId()).getDiscount());
                    i.setVat(productMap.get(item.productId()).getVat());
                    i.setOrder(order);
                    return i;
                })
                .toList();

        order.setOrderItems(orderItems);

        BigDecimal total = orderItems.stream()
                .map(OrderItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse updateOrderItemsByOrderId(String uuid, String orderCode, AddOrderRequest request) {
        Order order = this.findOrderByUuidAndOrderCode(uuid,orderCode);
        ProductGrpcResponse response = productGrpcClient.getProductById(
                ProductGrpcRequest.newBuilder()
                        .addAllOrderItems(
                                request.items().stream()
                                        .map(item -> OrderItems.newBuilder()
                                                .setProductId(item.productId())
                                                .setQuantity(item.quantity())
                                                .build())
                                        .toList()
                        ).build());

        Map<Long, ProductGrpc> productMap = response.getProductsList().stream()
                .collect(Collectors.toMap(ProductGrpc::getId, p -> p));


        Map<Long, OrderItem> existingItems = order.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItem::getProductId, i -> i));

        for (var reqItem : request.items()) {
            OrderItem existing = existingItems.get(reqItem.productId());

            if (existing != null) {
                // Có → cập nhật quantity (cộng thêm)
                existing.setPrice(existing.getPrice());
                existing.setQuantity(existing.getQuantity() + reqItem.quantity());
            } else {
                // Không có → thêm mới
                ProductGrpc p = productMap.get(reqItem.productId());
                OrderItem item = new OrderItem();
                item.setProductId(reqItem.productId());
                item.setProductName(p.getName());
                item.setPrice(new BigDecimal(p.getPrice()));
                item.setQuantity(Long.valueOf(reqItem.quantity()));
                item.setDiscount(p.getDiscount());
                item.setVat(p.getVat());
                item.setOrder(order);
                order.getOrderItems().add(item);
            }
        }

        BigDecimal total = order.getOrderItems().stream()
                .map(OrderItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));
    }

    @Override
    public Order findOrderByUuidAndOrderCode(String uuid, String orderCode) {
        return orderRepository.findOrderByCodeAndCustomerUuid(orderCode,UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException(String.format("Không tìm thấy đơn hàng %s", orderCode)));
    }

    @Override
    public Page<OrderResponse> getAllOrders(String uuid, Pageable pageable) {
        return orderRepository.getAllByCustomerUuid(UUID.fromString(uuid), pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    @Transactional
    public PaymentResponse paymentOrderByCode(String uuid, PaymentOrderRequest request) {
        Order order = this.findOrderByUuidAndOrderCode(uuid, request.code());
        order.setStatus(OrderStatus.PROCESSING); // Cập nhật trạng thái đang xử lý
        SePayPaymentResponse sePayResponse = null;

        // Tạo đơn hàng và xuất kho trước khi khách hàng thanh toán hoặc đặt hàng
        switch (request.paymentStatus()){
            case CASH_ON_DELIVERY -> {
                // Nếu khách thanh toán khi nhận hàng thì chuyển trạng thái
                order.setPaymentMethod(OrderPaymentStatus.CASH_ON_DELIVERY); // Chọn hình thức thanh toán khi nhận hàng
            }
            case BANK_TRANSFER_PAYMENT -> {
                // Nếu khách thanh toán bằng chuyển khoản phải chờ thanh toán và tạo mã QR thanh toán
                order.setPaymentMethod(OrderPaymentStatus.BANK_TRANSFER_PAYMENT);

                sePayResponse = sePayService.createCheckoutUrl(
                        order.getTotalPrice(),
                        order.getCode()
                );
            }
        }

        assert sePayResponse != null;
        ReservationInventoryGrpcResponse response = inventoryGrpcClient.reservationInventory(ReservationInventoryRequest.newBuilder()
                .setOrderCode(order.getCode())
                .addAllListProducts(order.getOrderItems().stream()
                        .map(item -> ListProducts.newBuilder()
                                .setProductId(item.getProductId())
                                .setQuantity(item.getQuantity())
                                .build())
                        .collect(Collectors.toList())
                )
                .build());

        if(!response.getSuccess()){
            throw new InternalServerException(response.getMessage());
        }

        orderRepository.save(order);
        return new PaymentResponse(
                order.getCode(),
                sePayResponse.qrCodeUrl(),
                sePayResponse.amount(),
                sePayResponse.secondsLeft()
        );
    }

    @Override
    @Transactional
    public void paymentOrderByTransfer(SePayWebhookRequest request) {
        Order order = orderRepository.findOrderByCode(request.code());
        if(!order.getTotalPrice().stripTrailingZeros().equals(request.transferAmount())){
            order.setPaymentStatus(PaymentStatus.FAILED);
            throw new InternalServerException("Giá không hợp lệ");
        }
        order.setPaymentStatus(PaymentStatus.PAID);

        orderRepository.save(order);

        // Đặt hàng và xuất kho
        ExportInventoryGrpcResponse response = inventoryGrpcClient.exportInventory(ExportInventoryRequest.newBuilder()
                .setOrderCode(order.getCode())
                .addAllListProducts(order.getOrderItems().stream()
                        .map(item -> ListProducts.newBuilder()
                                .setProductId(item.getProductId())
                                .setQuantity(item.getQuantity())
                                .build())
                        .collect(Collectors.toList())
                )
                .build());

        if(!response.getSuccess()){
            throw new InternalServerException(response.getMessage());
        }

        // Thông báo qua mail đã đặt hàng thành công
        notificationEventProducer.send(
                new TransferNotificationEvent(
                        order.getCustomerUuid(),
                        order.getPaymentMethod().getDescription(),
                        order.getTotalPrice(),
                        orderMapper.toEventList(order.getOrderItems()),
                        request.transactionDate(),
                        request.referenceCode(),
                        request.code()
                ),
                "transfer-payment-customer-events"
        );
        // Tạo đơn hàng
    }

    @Override
    public void orderCancel(String uuid, String orderCode) {
        Order order = this.findOrderByUuidAndOrderCode(uuid, orderCode);
        RestoreInventoryGrpcResponse response = inventoryGrpcClient.restoreInventory(RestoreInventoryRequest.newBuilder()
                .setOrderCode(orderCode)
                .build());

        if(!response.getSuccess()){
            throw new InternalServerException(response.getMessage());
        }

        order.setStatus(OrderStatus.CANCELLED);
    }

}
