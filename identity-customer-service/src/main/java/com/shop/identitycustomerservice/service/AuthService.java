package com.shop.identitycustomerservice.service;

import com.shop.identitycustomerservice.dto.request.RegisterAccountRequest;
import com.shop.identitycustomerservice.dto.response.RegisterAccountResponse;
import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.repository.CustomerAccountRepository;
import com.shop.identitycustomerservice.service.interfaces.IAuthService;
import com.shop.identitycustomerservice.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final CustomerAccountRepository customerAccountRepository;
    private final AuthValidator authValidator;

    @Override
    public RegisterAccountResponse registerAccount(RegisterAccountRequest request) {
        // 1. Kiểm tra validation
        authValidator.registerValidation(request);

        //2. Tạo tài khoản
        CustomerAccount customerAccount = CustomerAccount.builder()
                .email(request.email())
                .passwordHash(request.password())
                .build();

        //3.
        return null;
    }

}
