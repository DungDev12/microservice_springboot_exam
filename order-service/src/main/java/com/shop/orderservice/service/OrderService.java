package com.shop.orderservice.service;

import com.shop.orderservice.dto.request.CreateOrderRequest;
import com.shop.orderservice.dto.response.OrderResponse;
import com.shop.orderservice.repository.OrderRepository;
import com.shop.orderservice.service.interfaces.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        return null;
    }
}
