package com.shop.inventoryservice.dto.request;

import com.shop.inventoryservice.common.enums.TransactionType;

public record UpdateInventoryRequest(
        Long productId,
        Integer quantity,
        Integer reservedQuantity,
        TransactionType type,
        String referenceType,
        String note
) {
}
