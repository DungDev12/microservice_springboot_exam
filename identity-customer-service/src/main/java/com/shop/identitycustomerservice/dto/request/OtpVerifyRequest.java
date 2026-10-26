package com.shop.identitycustomerservice.dto.request;

public record OtpVerifyRequest(
        String token,
        String otp
) {
}
