package com.shop.identitycustomerservice.controller;

import com.shop.identitycustomerservice.dto.request.*;
import com.shop.identitycustomerservice.dto.response.ApiResponse;
import com.shop.identitycustomerservice.dto.response.RegisterAccountResponse;
import com.shop.identitycustomerservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("verify-otp/resend")
    public ResponseEntity<ApiResponse<?>> resendOtpWithEmail(
            @RequestParam("email") String email
    ){
        authService.resendVerificationCode(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        null,
                        "Gửi lại mã xác thực OTP thành công",
                        HttpStatus.OK
                ));
    }

    @PostMapping("verify-otp")
    public ResponseEntity<ApiResponse<?>> verifyOtp(
            @RequestBody OtpVerifyRequest request
            ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(authService.otpVerify(request),
                        "Xác thực thành công", HttpStatus.OK));
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

    @PostMapping("password/reset")
    public ResponseEntity<ApiResponse<?>> forgetPassword(
            @Valid @RequestParam ResetPasswordOTPRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(authService.resetPasswordOTP(request),
                        "Gửi mã khôi phục OTP thành công", HttpStatus.OK));
    }
}
