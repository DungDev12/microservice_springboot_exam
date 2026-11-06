package com.shop.inventoryservice.mapper;

import com.shop.inventoryservice.dto.response.InventoryTransactionsResponse;
import com.shop.inventoryservice.entity.InventoryTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InventoryTransactionsMapper {

    InventoryTransactionsResponse toResponse(InventoryTransaction inventoryTransaction);
}
