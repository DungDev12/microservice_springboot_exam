package com.shop.identityemployeeservice.service.interfaces;

import com.shop.identityemployeeservice.common.interfaces.ICrudService;
import com.shop.identityemployeeservice.dto.request.RoleRequest;
import com.shop.identityemployeeservice.dto.response.RoleResponse;
import com.shop.identityemployeeservice.entity.Role;

import java.util.List;

public interface IRoleService extends ICrudService<RoleRequest, RoleResponse, Long> {

    boolean existsByName(String name);

    Role getById(Long id);

    List<Role> findAllRoles();
}
