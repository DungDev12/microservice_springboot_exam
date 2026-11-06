package com.shop.inventoryservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shop.inventoryservice.common.enums.TransactionType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InventoryTransactionsResponse(
        Long id,
        TransactionType type,
        Integer quantityChange,
        Integer beforeChange,
        Integer afterChange,
        String reference,
        String referenceType,
        String note
) {
}
