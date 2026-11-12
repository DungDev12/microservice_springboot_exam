package com.shop.employeeservice.mapper;

import com.shop.employeeservice.dto.request.UpdateInfoRequest;
import com.shop.employeeservice.dto.response.EmployeeResponse;
import com.shop.employeeservice.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    EmployeeResponse toResponse(Employee employee);

    void update(UpdateInfoRequest request, @MappingTarget Employee employee);
}
