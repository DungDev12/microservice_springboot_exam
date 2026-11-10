package com.shop.identityemployeeservice.mapper;

import com.shop.identityemployeeservice.dto.request.RoleRequest;
import com.shop.identityemployeeservice.dto.response.RoleResponse;
import com.shop.identityemployeeservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    Role toEntity(RoleRequest request);
    RoleResponse toResponse(Role role);

    void update(RoleRequest request, @MappingTarget Role entity);

    RoleResponse top();
