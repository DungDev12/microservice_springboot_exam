package com.shop.inventoryservice.mapper;

import com.shop.inventoryservice.dto.response.InventoryTransactionsResponse;
import com.shop.inventoryservice.entity.InventoryTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventoryTransactionsMapper {

    InventoryTransactionsResponse toResponse(InventoryTransaction inventoryTransaction);
}
