package com.shop.identitycustomerservice.dto.response;

public record ResetPasswordOTPResponse(
        boolean status,
        String path,
        String nextStep,
        String resetToken
) {
}
