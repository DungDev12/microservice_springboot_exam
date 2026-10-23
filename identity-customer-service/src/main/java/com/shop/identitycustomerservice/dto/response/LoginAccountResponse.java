package com.shop.identitycustomerservice.dto.response;

public record LoginAccountResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
