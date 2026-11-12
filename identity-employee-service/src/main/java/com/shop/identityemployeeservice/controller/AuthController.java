package com.shop.identityemployeeservice.controller;

import com.shop.identityemployeeservice.dto.request.auth.CreateEmployeeRequest;
import com.shop.identityemployeeservice.dto.request.auth.LoginRequest;
import com.shop.identityemployeeservice.dto.response.ApiResponse;
import com.shop.identityemployeeservice.dto.response.auth.CreateEmployeeResponse;
import com.shop.identityemployeeservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(authService.login(request),"Đăng nhâp thành công", HttpStatus.OK));
    }

}
