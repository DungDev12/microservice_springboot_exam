package com.shop.orderservice.dto.response;


import java.math.BigDecimal;
import java.time.Instant;

public record SePayPaymentResponse(
        String orderCode,
        BigDecimal amount,
        String qrCodeUrl,
        Long secondsLeft,
        Instant createdAt,
        Instant expiredAt
) {
}
