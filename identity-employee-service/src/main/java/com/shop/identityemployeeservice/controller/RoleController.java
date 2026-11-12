package com.shop.identityemployeeservice.controller;

import com.shop.identityemployeeservice.dto.request.role.AddRoleRequest;
import com.shop.identityemployeeservice.dto.request.role.RoleRequest;
import com.shop.identityemployeeservice.dto.response.ApiResponse;
import com.shop.identityemployeeservice.dto.response.roles.RoleResponse;
import com.shop.identityemployeeservice.entity.Role;
import com.shop.identityemployeeservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getRoles(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(roleService.getAllRoles(),
                        "Lấy danh sách quyền",
                        HttpStatus.OK));
    }

    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @PostMapping()
    public ResponseEntity<ApiResponse<RoleResponse>> addRole(
            @RequestHeader("X-Auth-UserUuid") String userId,
            @RequestBody RoleRequest roleRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(roleService.create(userId,roleRequest),
                        "Tạo vài trò thành công",
                        HttpStatus.CREATED));
    }

    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @RequestHeader("X-Auth-UserUuid") String userId,
            @PathVariable Long id,
            @RequestBody RoleRequest roleRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(roleService.update(userId,id, roleRequest),
                        "Cập nhật thành công",
                        HttpStatus.OK));
    }

    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRole(
            @PathVariable Long id
    ){
        roleService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(null);
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<?>> addPermissions(
            @PathVariable Long id,
            @RequestBody AddRoleRequest request
            ){
        roleService.updatePermissions(id,request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Thêm quyền thành công"));
    }
}
