package com.shop.identitycustomerservice.service.interfaces;

import com.shop.identitycustomerservice.dto.request.RegisterAccountRequest;
import com.shop.identitycustomerservice.dto.response.RegisterAccountResponse;

public interface IAuthService {
    RegisterAccountResponse registerAccount(RegisterAccountRequest request);

    boolean checkEmail(String email);
}
