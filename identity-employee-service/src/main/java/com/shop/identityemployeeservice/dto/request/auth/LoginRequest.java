package com.shop.identityemployeeservice.dto.request.auth;

public record LoginRequest(
        String username,
        String password
) {
}
