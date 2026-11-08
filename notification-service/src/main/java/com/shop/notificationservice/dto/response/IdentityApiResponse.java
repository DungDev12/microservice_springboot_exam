package com.shop.notificationservice.dto.response;

public record IdentityApiResponse(
        int status,
        boolean success,
        String message,
        AccountEmailResponse data, // Khớp với object "data" trong JSON của bạn
        String timestamp
) {
    public record AccountEmailResponse(
            String email
    ){}
}
