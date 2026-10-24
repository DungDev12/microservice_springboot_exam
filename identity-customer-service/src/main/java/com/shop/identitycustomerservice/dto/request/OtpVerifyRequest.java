package com.shop.identitycustomerservice.dto.request;

public record OtpVerifyRequest(
        String accessToken,
        String email,
        String otp
) {
}
