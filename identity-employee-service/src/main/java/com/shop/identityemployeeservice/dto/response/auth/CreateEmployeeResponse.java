package com.shop.identityemployeeservice.dto.response.auth;

public record CreateEmployeeResponse(
        String username,
        String password,
        String fullName
) {
}
