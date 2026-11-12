package com.shop.identityemployeeservice.service;

import com.shop.identityemployeeservice.dto.request.role.AddRoleRequest;
import com.shop.identityemployeeservice.dto.request.role.RoleRequest;
import com.shop.identityemployeeservice.dto.response.roles.RoleResponse;
import com.shop.identityemployeeservice.entity.Permission;
import com.shop.identityemployeeservice.entity.Role;
import com.shop.identityemployeeservice.exception.AlreadyExistsException;
import com.shop.identityemployeeservice.exception.InternalServerException;
import com.shop.identityemployeeservice.exception.NotFoundException;
import com.shop.identityemployeeservice.mapper.RoleMapper;
import com.shop.identityemployeeservice.repository.RoleRepository;
import com.shop.identityemployeeservice.service.interfaces.IRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final PermissionService permissionService;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse create(String uuid, RoleRequest request) {
        if(this.existsByName(request.name())) {
            throw new AlreadyExistsException("Tên vai trò đã tồn tại");
        }
        Role role = roleMapper.toEntity(request);
        role.setCreatedBy(UUID.fromString(uuid));
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse update(String uuid, Long aLong, RoleRequest request) {
        Role role = this.getById(aLong);
        if(!role.getName().equals(request.name())) {
            if(this.existsByName(request.name())) {
                throw new AlreadyExistsException("Tên vai trò đã tồn tại");
            }
        }
        role.setVersion(role.getVersion() + 1);
        role.setCreatedBy(UUID.fromString(uuid));
        roleMapper.update(request,role);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    public void delete(Long aLong) {
        roleRepository.deleteById(aLong);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Không tìm thấy vai trò này")
        );
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAllByIsDeletedFalse()
                .stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Override
    public void updatePermissions(Long roleId, AddRoleRequest request) {
        Role role = this.getById(roleId);
        List<Permission> permissions =
                permissionService.getPermissionByIds(request.permissionIds());

        // Kiểm tra các Permission ID có tồn tại đầy đủ không
        if (permissions.size() != request.permissionIds().size()) {
            throw new InternalServerException(
                    "Một hoặc nhiều Permission không tồn tại"
            );
        }
        role.setVersion(role.getVersion() + 1);
        role.setPermissions(new HashSet<>(permissions));
        roleRepository.save(role);
    }

    @Transactional
    @Override
    public List<String> getPermissionByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Tên vai trò không tồn tại"));
        return role.getPermissions()
                .stream()
                .map(Permission::getName)
                .toList();
    }
}
