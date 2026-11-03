package com.shop.inventoryservice.mapper;

import com.shop.inventoryservice.dto.request.UpdateInventoryRequest;
import com.shop.inventoryservice.dto.response.InventoryResponse;
import com.shop.inventoryservice.entity.Inventory;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryMapper {

    InventoryResponse toResponse(Inventory inventory);

    void updateInventory(
            UpdateInventoryRequest request,
            @MappingTarget Inventory inventory);
}
