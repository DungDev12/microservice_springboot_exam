package com.shop.identityemployeeservice.dto.response.roles;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleResponse(
        Long id,
        String name,
        String description,
        boolean active) {
}
