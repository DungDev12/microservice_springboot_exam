package com.shop.identityemployeeservice.service;

import com.shop.identityemployeeservice.dto.request.RoleRequest;
import com.shop.identityemployeeservice.dto.response.RoleResponse;
import com.shop.identityemployeeservice.entity.Role;
import com.shop.identityemployeeservice.exception.AlreadyExistsException;
import com.shop.identityemployeeservice.exception.NotFoundException;
import com.shop.identityemployeeservice.mapper.RoleMapper;
import com.shop.identityemployeeservice.repository.RoleRepository;
import com.shop.identityemployeeservice.service.interfaces.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {
        if(this.existsByName(request.name())) {
            throw new AlreadyExistsException("Tên quyền đã tồn tại");
        }
        Role role =  roleMapper.toEntity(request);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse update(Long aLong, RoleRequest request) {
        Role role = this.getById(aLong);
        if(this.existsByName(request.name())) {
            throw new AlreadyExistsException("Tên quyền đã tồn tại");
        }
        roleMapper.update(request,role);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    @Override
    public void delete(Long aLong) {
        Role role = this.getById(aLong);
        role.setDeleted(true);
        roleRepository.save(role);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Không tìm thấy quyền này")
        );
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAllByIsDeletedFalse();
    }
}
