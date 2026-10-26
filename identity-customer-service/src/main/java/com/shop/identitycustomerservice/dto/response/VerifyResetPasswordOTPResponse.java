package com.shop.identitycustomerservice.dto.response;

public record VerifyResetPasswordOTPResponse(
        boolean status,
        String path,
        String nextStep,
        String resetToken
) {
}
