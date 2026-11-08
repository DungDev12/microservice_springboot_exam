package com.shop.orderservice.service.interfaces;

import com.shop.orderservice.dto.request.AddOrderRequest;
import com.shop.orderservice.dto.request.CreateOrderRequest;
import com.shop.orderservice.dto.request.PaymentOrderRequest;
import com.shop.orderservice.dto.request.SePayWebhookRequest;
import com.shop.orderservice.dto.response.OrderResponse;
import com.shop.orderservice.dto.response.PaymentResponse;
import com.shop.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {

    OrderResponse createOrder(String uuid, CreateOrderRequest request);

    OrderResponse updateOrderItemsByOrderId(String uuid, String orderCode, AddOrderRequest request);

    Order findOrderById(Long id);
    Order findOrderByUuidAndOrderCode(String uuid, String orderCode);

    Page<OrderResponse> getAllOrders(String uuid, Pageable pageable);

    PaymentResponse paymentOrderByCode(String uuid, PaymentOrderRequest request);

    void paymentOrderByTransfer(SePayWebhookRequest request);

    void orderCancel(String uuid, String orderCode);
}
