package com.shop.orderservice.dto.request;

import com.shop.orderservice.common.enums.OrderPaymentStatus;

public record PaymentOrderRequest(
        OrderPaymentStatus paymentStatus,
        String code
) {
}
