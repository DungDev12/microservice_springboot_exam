package com.shop.inventoryservice.mapper;

import com.shop.inventoryservice.dto.response.InventoryTransactionsResponse;
import com.shop.inventoryservice.entity.InventoryTransaction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InventoryTransactionsMapper {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    InventoryTransactionsResponse toResponse(InventoryTransaction inventoryTransaction);
}
