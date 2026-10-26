package com.shop.identitycustomerservice.dto.request;

public record ConfirmResetPasswordRequest(
        String token,
        String password
) {
}
