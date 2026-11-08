package com.shop.orderservice.dto.request;

import java.util.List;

public record AddOrderRequest(
        List<OrderItems> items
) {
    public record OrderItems(
            Long productId,
            Integer quantity
    ){}
}
