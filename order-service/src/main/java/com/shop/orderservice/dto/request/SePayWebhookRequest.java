package com.shop.orderservice.dto.request;

import java.math.BigDecimal;

public record SePayWebhookRequest(
        String code,
        BigDecimal transferAmount,
        String transactionDate,
        String referenceCode
) {
}
