package com.shop.customerservice.mapper;

import com.shop.customerservice.dto.request.AddressRequest;
import com.shop.customerservice.dto.response.AddressResponse;
import com.shop.customerservice.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {

    Address toEntity(AddressRequest addressRequest);

    AddressResponse toResponse(Address address);

    void update(AddressRequest addressRequest, @MappingTarget Address address);
}
