package com.shop.identitycustomerservice.validator;

import com.shop.identitycustomerservice.common.enums.StatusAccount;
import com.shop.identitycustomerservice.dto.request.LoginAccountRequest;
import com.shop.identitycustomerservice.dto.request.RegisterAccountRequest;
import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.exception.AccessDeniedException;
import com.shop.identitycustomerservice.exception.AccountNotVerifiedException;
import com.shop.identitycustomerservice.repository.CustomerAccountRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final CustomerAccountRepository customerAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerValidation(RegisterAccountRequest request){
        checkEmail(request.email());
    }

    /** Xử lý validation đăng nhập
     * Ví dụ:
     *  - Tài khoản đúng mật khẩu không?
     *  - Tài khoản có bị blocked không?
     *  - Tài khoản có acctive chưa ....
     * */
    public void loginValidation(CustomerAccount account, LoginAccountRequest request) {
        // Kiểm tra mật khẩu
        if(!checkPassword(request.password(), account.getPasswordHash())){
            throw new AccessDeniedException("Mật khẩu không khớp", HttpStatus.UNAUTHORIZED);
        }
        // Kiểm tra trạng thái
        checkStatus(account.getStatus());
    }

    // Kiểm tra email có tồn tại không
    public void checkEmail(String email){
        if(customerAccountRepository.existsByEmailIgnoreCase(email)) {
            throw new ValidationException("Email already exists");
        }
    }

    public void checkStatus(StatusAccount status){
        if (status == StatusAccount.PENDING_VERIFICATION) {
            throw new AccountNotVerifiedException("Tài khoản chưa được xác thực");
        }
    }


    // Kiểm tra mật khẩu có trùng không
    private boolean checkPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
