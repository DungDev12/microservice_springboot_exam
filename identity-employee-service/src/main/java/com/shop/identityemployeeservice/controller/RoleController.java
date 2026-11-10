package com.shop.identityemployeeservice.controller;

import com.shop.identityemployeeservice.dto.request.RoleRequest;
import com.shop.identityemployeeservice.dto.response.ApiResponse;
import com.shop.identityemployeeservice.dto.response.RoleResponse;
import com.shop.identityemployeeservice.entity.Role;
import com.shop.identityemployeeservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Role>>> getRoles(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(roleService.findAllRoles(),
                        "Lấy danh sách quyền",
                        HttpStatus.OK));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<RoleResponse>> addRole(
            @RequestHeader("X-Auth-Userid") String userId,
            @RequestHeader("X-Auth-Roles") String roles,
            @RequestBody RoleRequest roleRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(null);
    }
}
