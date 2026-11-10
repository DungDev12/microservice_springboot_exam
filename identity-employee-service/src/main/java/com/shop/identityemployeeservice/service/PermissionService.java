package com.shop.identityemployeeservice.service;

import com.shop.identityemployeeservice.dto.request.permission.PermissionRequest;
import com.shop.identityemployeeservice.dto.response.permission.PermissionResponse;
import com.shop.identityemployeeservice.entity.Permission;
import com.shop.identityemployeeservice.exception.AlreadyExistsException;
import com.shop.identityemployeeservice.exception.NotFoundException;
import com.shop.identityemployeeservice.mapper.PermissionMapper;
import com.shop.identityemployeeservice.repository.PermissionRepository;
import com.shop.identityemployeeservice.service.interfaces.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PermissionResponse create(String uuid, PermissionRequest request) {
        Permission permission = permissionMapper.toEntity(request);
        permission.setCreatedBy(UUID.fromString(uuid));
        return permissionMapper.toResponse(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse update(String uuid, Long aLong, PermissionRequest request) {
        Permission permission = this.getById(aLong);
        if(this.existsByName(request.name())) {
            throw new AlreadyExistsException("Tên vai trò đã tồn tại");
        }
        permissionMapper.update(request, permission);
        permission.setCreatedBy(UUID.fromString(uuid));
        return permissionMapper.toResponse(permissionRepository.save(permission));
    }

    @Override
    public void delete(Long aLong) {
        permissionRepository.deleteById(aLong);
    }

    @Override
    public boolean existsByName(String name) {
        return permissionRepository.existsByName(name);
    }

    @Override
    public Permission getById(Long aLong) {
        return permissionRepository.findById(aLong).orElseThrow(
                () -> new NotFoundException("Không tìm thấy quyền")
        );
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAllByIsDeletedFalse()
                .stream()
                .map(permissionMapper::toResponse)
                .toList();
    }
}
