package com.shop.orderservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderPaymentStatus {
    CASH_ON_DELIVERY("Thanh toán tiền mặt"),
    BANK_TRANSFER_PAYMENT("Thanh toán chuyển khoản");

    private final String description;

}
