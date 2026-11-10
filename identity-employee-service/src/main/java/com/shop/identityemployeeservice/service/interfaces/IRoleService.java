package com.shop.identityemployeeservice.service.interfaces;

import com.shop.identityemployeeservice.common.interfaces.ICrudService;
import com.shop.identityemployeeservice.dto.request.role.RoleRequest;
import com.shop.identityemployeeservice.dto.response.roles.RoleResponse;
import com.shop.identityemployeeservice.entity.Role;

import java.util.List;

public interface IRoleService extends ICrudService<RoleRequest, RoleResponse, Long> {

    boolean existsByName(String name);

    Role getById(Long id);

    List<RoleResponse> getAllRoles();
}
