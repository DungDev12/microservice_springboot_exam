package com.shop.orderservice.service;

import com.shop.orderservice.dto.request.CreateOrderRequest;
import com.shop.orderservice.dto.response.OrderResponse;
import com.shop.orderservice.entity.Order;
import com.shop.orderservice.mapper.OrderMapper;
import com.shop.orderservice.repository.OrderRepository;
import com.shop.orderservice.service.interfaces.IOrderService;
import com.shop.orderservice.utils.OrderCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderCodeGenerator orderCodeGenerator;


    @Override
    public OrderResponse createOrder(String uuid, CreateOrderRequest request) {
        // Khách hàng đặt đơn tạo đơn hàng
        Order order = orderMapper.toEntity(request);
        order.setCustomerUuid(UUID.fromString(uuid));
        order.setCode(orderCodeGenerator.generate());

        // Thêm sản phẩm mua vào
        return orderMapper.toResponse(orderRepository.save(order));
    }
}
