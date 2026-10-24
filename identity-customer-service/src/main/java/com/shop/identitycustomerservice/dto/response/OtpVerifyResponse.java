package com.shop.identitycustomerservice.dto.response;

public record OtpVerifyResponse(
        boolean verifyStatus,
        String message
) {
}
