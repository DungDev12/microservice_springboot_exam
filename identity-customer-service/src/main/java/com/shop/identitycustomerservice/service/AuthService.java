package com.shop.identitycustomerservice.service;

import com.shop.identitycustomerservice.common.enums.StatusAccount;
import com.shop.identitycustomerservice.dto.request.RegisterAccountRequest;
import com.shop.identitycustomerservice.dto.response.RegisterAccountResponse;
import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.repository.CustomerAccountRepository;
import com.shop.identitycustomerservice.service.interfaces.IAuthService;
import com.shop.identitycustomerservice.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final CustomerAccountRepository customerAccountRepository;
    private final AuthValidator authValidator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterAccountResponse registerAccount(RegisterAccountRequest request) {
        // 1. Kiểm tra validation
        authValidator.registerValidation(request);

        //2. Tạo tài khoản
        CustomerAccount customerAccount = CustomerAccount.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .status(StatusAccount.CREATED)
                .build();

        //3. Lưu tài khoản
        customerAccountRepository.save(customerAccount);
        return null;
    }

    /** Kiểm tra email có tồn tại không?
    * @return {@code true} tài khoản có thể đăng ký được
     * @throws Exception Tài khoản bị trùng
    */
    @Override
    public boolean checkEmail(String email) {
        authValidator.checkEmail(email);
        return true;
    }


}
