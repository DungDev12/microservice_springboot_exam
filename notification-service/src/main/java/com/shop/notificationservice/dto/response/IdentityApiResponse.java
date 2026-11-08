package com.shop.notificationservice.dto.response;

public record IdentityApiResponse(
        int status,
        boolean success,
        String message,
        AccountEmailResponse data,
        String timestamp
) {
    public record AccountEmailResponse(
            String email
    ) {}
}