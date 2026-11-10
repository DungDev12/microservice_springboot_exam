package com.shop.identityemployeeservice.controller;

import com.shop.identityemployeeservice.dto.request.permission.PermissionRequest;
import com.shop.identityemployeeservice.dto.response.ApiResponse;
import com.shop.identityemployeeservice.dto.response.permission.PermissionResponse;
import com.shop.identityemployeeservice.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getRoles(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(permissionService.getAllPermissions(),
                        "Lấy danh sách quyền",
                        HttpStatus.OK));
    }

    @PreAuthorize("hasAuthority('CREATE_PERMISSION')")
    @PostMapping()
    public ResponseEntity<ApiResponse<PermissionResponse>> addRole(
            @RequestHeader("X-Auth-Userid") String userId,
            @RequestBody PermissionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(permissionService.create(userId, request),
                        "Tạo quyền thành công",
                        HttpStatus.CREATED));
    }

    @PreAuthorize("hasAuthority('UPDATE_PERMISSION')")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponse>> updateRole(
            @RequestHeader("X-Auth-Userid") String userId,
            @PathVariable Long id,
            @RequestBody PermissionRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(permissionService.update(userId, id, request),
                        "Tạo quyền thành công",
                        HttpStatus.OK));
    }

    @PreAuthorize("hasAuthority('DELETE_PERMISSION')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRole(
            @PathVariable Long id
    ){
        permissionService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
