package com.shop.inventoryservice.dto.response;

public record InventoryResponse(
        Long id,
        Long productId,
        Integer quantity,
        Integer reservedQuantity
) {
}
