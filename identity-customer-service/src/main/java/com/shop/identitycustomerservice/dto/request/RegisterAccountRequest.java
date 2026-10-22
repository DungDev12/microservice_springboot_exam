package com.shop.identitycustomerservice.dto.request;

import com.shop.identitycustomerservice.common.enums.TypeRegisterAccount;

public record RegisterAccountRequest(
        String email,
        String password,
        String phoneNumber,
        TypeRegisterAccount type
) {
}
