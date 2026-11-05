package com.shop.orderservice.service.interfaces;

import com.shop.orderservice.dto.request.CreateOrderRequest;
import com.shop.orderservice.dto.response.OrderResponse;

public interface IOrderService {

    OrderResponse createOrder(CreateOrderRequest request);
}
