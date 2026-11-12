package com.shop.identityemployeeservice.mapper;

import com.shop.identityemployeeservice.dto.request.role.RoleRequest;
import com.shop.identityemployeeservice.dto.response.roles.RoleResponse;
import com.shop.identityemployeeservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    Role toEntity(RoleRequest request);

    RoleResponse toResponse(Role role);

    void update(RoleRequest request, @MappingTarget Role entity);

}