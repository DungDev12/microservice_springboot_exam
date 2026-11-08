package com.shop.orderservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING_PAYMENT("Chờ thanh toán"),
    PROCESSING("Đang xử lý"),
    COMPLETED("Hoàn thành"),
    PAYMENT_EXPIRED("Thanh toán hết hạn"),
    PAYMENT_FAILED("Thanh toán thất bại"),
    CANCELLED("Đã hủy");

    private final String description;
}
