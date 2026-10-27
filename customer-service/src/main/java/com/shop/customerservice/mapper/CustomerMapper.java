package com.shop.customerservice.mapper;

import com.shop.customerservice.dto.request.UpdateProfileRequest;
import com.shop.customerservice.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    void updateProfileFromDto(UpdateProfileRequest dto, @MappingTarget Customer customer);
}
