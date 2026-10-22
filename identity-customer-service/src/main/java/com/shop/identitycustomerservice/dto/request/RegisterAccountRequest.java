package com.shop.identitycustomerservice.dto.request;

public record RegisterAccountRequest(
        String email,
        String password
) {
}
