package com.shop.orderservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    UNPAID("Chưa thanh toán"),
    PAID("Đã thanh toán"),
    FAILED("Thanh toán thất bại"),
    EXPIRED("Thanh toán hết hạn");

    private final String description;
}