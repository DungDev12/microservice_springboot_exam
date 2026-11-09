package com.shop.customerservice.mapper;

import com.shop.customerservice.dto.request.CustomerAddressRequest;
import com.shop.customerservice.dto.response.CustomerAddressResponse;
import com.shop.customerservice.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {

    Address toEntity(CustomerAddressRequest request);
    CustomerAddressResponse toResponse(Address address);

    void updateAddress(CustomerAddressRequest request, @MappingTarget Address address);
}
