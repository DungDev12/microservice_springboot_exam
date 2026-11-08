package com.shop.orderservice.service.interfaces;

import com.shop.orderservice.dto.response.SePayPaymentResponse;

import java.math.BigDecimal;

public interface ISePayService {
    SePayPaymentResponse createCheckoutUrl(BigDecimal amount, String orderCode);
}
