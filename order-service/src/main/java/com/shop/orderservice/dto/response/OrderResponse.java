package com.shop.orderservice.dto.response;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        String recipientName,
        String recipientPhoneNumber,
        String recipientAddress,
        String code,
        BigDecimal totalPrice) {
}
