package com.shop.inventoryservice.mapper;

import com.shop.inventoryservice.dto.request.UpdateInventoryRequest;
import com.shop.inventoryservice.dto.response.InventoryResponse;
import com.shop.inventoryservice.entity.Inventory;
import com.shop.inventoryservice.entity.InventoryTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventoryMapper {

    InventoryResponse toResponse(Inventory inventory);

    void updateInventory(
            UpdateInventoryRequest request,
            @MappingTarget Inventory inventory);
}
