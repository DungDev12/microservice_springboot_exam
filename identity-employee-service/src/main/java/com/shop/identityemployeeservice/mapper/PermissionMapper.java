package com.shop.identityemployeeservice.mapper;

import com.shop.identityemployeeservice.dto.request.permission.PermissionRequest;
import com.shop.identityemployeeservice.dto.response.permission.PermissionResponse;
import com.shop.identityemployeeservice.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {

    Permission toEntity(PermissionRequest request);
    PermissionResponse toResponse(Permission permission);

    void update(PermissionRequest request, @MappingTarget Permission permission);
}
