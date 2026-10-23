package com.shop.identitycustomerservice.controller;

import com.shop.identitycustomerservice.dto.request.CheckEmailRequest;
import com.shop.identitycustomerservice.dto.request.LoginAccountRequest;
import com.shop.identitycustomerservice.dto.request.RefreshTokenRequest;
import com.shop.identitycustomerservice.dto.request.RegisterAccountRequest;
import com.shop.identitycustomerservice.dto.response.ApiResponse;
import com.shop.identitycustomerservice.dto.response.RegisterAccountResponse;
import com.shop.identitycustomerservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginAccountRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(authService.loginAccount(request),
                        "Đăng nhập thành công",
                        HttpStatus.OK)
                );
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse<RegisterAccountResponse>> registerAccount(
            @Valid @RequestBody RegisterAccountRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(authService.registerAccount(request),
                        "Đăng ký thành công",
                        HttpStatus.CREATED)
                );
    }

    @PostMapping("check-email")
    public ResponseEntity<ApiResponse<?>> checkEmail(
            @Valid @RequestBody CheckEmailRequest email
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(authService.checkEmail(email),
                        "Tài khoản có thể tạo",
                        HttpStatus.OK)
                );
    }

    @PostMapping("refresh-token")
    public ResponseEntity<ApiResponse<?>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(authService.refreshAccount(request),
                        "Tạo token mới thành công",
                        HttpStatus.OK));
    }
}
