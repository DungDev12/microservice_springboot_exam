package com.shop.identitycustomerservice.service.interfaces;

import com.shop.identitycustomerservice.dto.request.CheckEmailRequest;
import com.shop.identitycustomerservice.dto.request.LoginAccountRequest;
import com.shop.identitycustomerservice.dto.request.RefreshTokenRequest;
import com.shop.identitycustomerservice.dto.request.RegisterAccountRequest;
import com.shop.identitycustomerservice.dto.response.LoginAccountResponse;
import com.shop.identitycustomerservice.dto.response.RegisterAccountResponse;

public interface IAuthService {
    RegisterAccountResponse registerAccount(RegisterAccountRequest request);
    LoginAccountResponse loginAccount(LoginAccountRequest request);
    LoginAccountResponse refreshAccount(RefreshTokenRequest request);

    boolean checkEmail(CheckEmailRequest request);
}
