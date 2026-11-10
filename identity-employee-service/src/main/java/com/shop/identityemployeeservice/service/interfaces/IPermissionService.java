package com.shop.identityemployeeservice.service.interfaces;

import com.shop.identityemployeeservice.common.interfaces.ICrudService;
import com.shop.identityemployeeservice.dto.request.permission.PermissionRequest;
import com.shop.identityemployeeservice.dto.response.permission.PermissionResponse;
import com.shop.identityemployeeservice.entity.Permission;

import java.util.List;

public interface IPermissionService extends ICrudService<PermissionRequest, PermissionResponse, Long> {

    boolean existsByName(String name);

    Permission getById(Long aLong);

    List<PermissionResponse> getAllPermissions();
}
