package com.shop.inventoryservice.dto.request;

public record UpdateInventoryRequest(
        Long productId,
        Integer quantity,
        Integer reservedQuantity,
        String type,
        String note
) {
}
