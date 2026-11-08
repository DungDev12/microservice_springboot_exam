package com.shop.orderservice.dto.response;

import java.math.BigDecimal;

public record PaymentResponse(
        String orderCode,
        String qrCodeUrl,
        BigDecimal amount,
        long defaultTimeoutSeconds
) {
}
