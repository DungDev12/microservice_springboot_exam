package com.shop.identityemployeeservice.service;

import com.shop.identityemployeeservice.dto.request.role.RoleRequest;
import com.shop.identityemployeeservice.dto.response.roles.RoleResponse;
import com.shop.identityemployeeservice.entity.Role;
import com.shop.identityemployeeservice.exception.AlreadyExistsException;
import com.shop.identityemployeeservice.exception.NotFoundException;
import com.shop.identityemployeeservice.mapper.RoleMapper;
import com.shop.identityemployeeservice.repository.RoleRepository;
import com.shop.identityemployeeservice.service.interfaces.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
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
        if(this.existsByName(request.name())) {
            throw new AlreadyExistsException("Tên vai trò đã tồn tại");
        }
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
}
