package com.shop.customerservice.mapper;

import com.shop.customerservice.dto.request.UpdateCustomerRequest;
import com.shop.customerservice.dto.response.CustomerResponse;
import com.shop.customerservice.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    CustomerResponse toResponse(Customer customer);

    void update(UpdateCustomerRequest request, @MappingTarget Customer customer);
}
