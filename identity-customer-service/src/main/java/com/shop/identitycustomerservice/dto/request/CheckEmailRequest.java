package com.shop.identitycustomerservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CheckEmailRequest(
        @Email(message = "Email không hợp lệ")
        @NotBlank(message = "Email không được để trống")
        String email
) {
}
