package com.shop.identitycustomerservice.dto.request;

public record ChangePasswordRequest(
        String accessToken,
        String password
) {
}
