package com.shop.orderservice.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record TransferNotificationEvent(
        UUID uuidCustomer,
        String paymentMethod,
        BigDecimal totalPrice,
        List<OrderItem> productOrders,
        String transactionDate,
        String referenceCode,
        String code
) {
    public record OrderItem(
            String name,
            Integer quantity,
            BigDecimal price,
            Integer discount,
            Integer vat,
            BigDecimal amount
    ){}
}
