package com.shop.identityemployeeservice.controller;

import com.shop.identityemployeeservice.dto.request.auth.CreateEmployeeRequest;
import com.shop.identityemployeeservice.dto.response.ApiResponse;
import com.shop.identityemployeeservice.dto.response.auth.CreateEmployeeResponse;
import com.shop.identityemployeeservice.service.AccountService;
import com.shop.identityemployeeservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class EmployeeController {

    private final AccountService accountService;

    @PreAuthorize("hasAuthority('CREATE_EMPLOYEE')")
    @PostMapping()
    public ResponseEntity<ApiResponse<CreateEmployeeResponse>> create(
            @RequestHeader("X-Auth-UserUuid") String userUuid,
            @RequestBody CreateEmployeeRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        accountService.createEmployee(userUuid,request),
                        "Tạo nhân viên thành công",
                        HttpStatus.OK
                ));
    }

    @PreAuthorize("hasAuthority('ADD_ROLE')")
    @PostMapping("/{uuid}/roles/{roleId}")
    public ResponseEntity<ApiResponse<CreateEmployeeResponse>> addRole(
            @PathVariable String uuid,
            @PathVariable Long roleId
    ){
        accountService.addRole(uuid,roleId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(null);
    }
}
