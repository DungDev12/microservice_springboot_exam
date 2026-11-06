package com.shop.orderservice.dto.request;

import java.util.List;

public record CreateOrderRequest(
        String recipientName,
        String recipientPhoneNumber,
        String recipientAddress,
        List<OrderItems> items
) {
    public record OrderItems(
            Long productId,
            Integer quantity
    ){}
}
