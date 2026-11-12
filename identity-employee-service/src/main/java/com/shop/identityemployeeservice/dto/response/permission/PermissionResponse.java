package com.shop.identityemployeeservice.dto.response.permission;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PermissionResponse(
        Long id,
        String name,
        String description,
        boolean active) {
}
