package com.shop.identitycustomerservice.dto.response;

public record RegisterAccountResponse(
        boolean createStatus,
        String path,
        String nextStep,
        String email,
        String accessToken,
        String refreshToken
) {
}
