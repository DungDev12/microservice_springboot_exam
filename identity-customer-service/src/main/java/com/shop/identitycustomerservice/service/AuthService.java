package com.shop.identitycustomerservice.service;

import com.shop.identitycustomerservice.common.enums.StatusAccount;
import com.shop.identitycustomerservice.config.jwt.JwtProvider;
import com.shop.identitycustomerservice.dto.request.CheckEmailRequest;
import com.shop.identitycustomerservice.dto.request.LoginAccountRequest;
import com.shop.identitycustomerservice.dto.request.RefreshTokenRequest;
import com.shop.identitycustomerservice.dto.request.RegisterAccountRequest;
import com.shop.identitycustomerservice.dto.response.LoginAccountResponse;
import com.shop.identitycustomerservice.dto.response.RegisterAccountResponse;
import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.entity.RefreshToken;
import com.shop.identitycustomerservice.event.NotificationEvent;
import com.shop.identitycustomerservice.event.NotificationEventProducer;
import com.shop.identitycustomerservice.exception.AccessDeniedException;
import com.shop.identitycustomerservice.repository.CustomerAccountRepository;
import com.shop.identitycustomerservice.repository.RefreshTokenRepository;
import com.shop.identitycustomerservice.service.interfaces.IAuthService;
import com.shop.identitycustomerservice.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final CustomerAccountRepository customerAccountRepository;
    private final RefreshTokenService  refreshTokenService;
    private final AuthValidator authValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final NotificationEventProducer notificationEventProducer;

    @Override
    public RegisterAccountResponse registerAccount(RegisterAccountRequest request) {
        // 1. Kiểm tra validation
        authValidator.registerValidation(request);

        //2. Tạo tài khoản
        CustomerAccount customerAccount = CustomerAccount.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .status(StatusAccount.PENDING_VERIFICATION)
                .build();

        //3. Lưu tài khoản
        customerAccountRepository.save(customerAccount);

        //4. Lưu thông tin khách hàng

        //5. Gửi email xác thực tài khoản
        notificationEventProducer.sendVerificationCode(
                NotificationEvent.builder()
                        .uuid(customerAccount.getUuid())
                        .message("mã OTP của bạn là 12j321")
                        .build()
        );
        return null;
    }

    @Override
    public LoginAccountResponse loginAccount(LoginAccountRequest request) {
        CustomerAccount customerAccount = customerAccountRepository.findByEmail(request.email())
                .orElseThrow(() -> new AccessDeniedException("Email không tồn tại", HttpStatus.UNAUTHORIZED));
        // 1. Kiểm tra validation
        authValidator.loginValidation(customerAccount, request);

        // 2. Cấp Tạo AccessToken và RefreshToken
        String accessToken = jwtProvider.generateAccessToken(customerAccount.getEmail(),customerAccount.getUuid());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customerAccount);
        return new LoginAccountResponse(accessToken, refreshToken.getToken(), "Bearer");
    }

    @Override
    public LoginAccountResponse refreshAccount(RefreshTokenRequest request) {
        //1. Tìm token
        RefreshToken refreshToken = refreshTokenService.findByRefreshToken(request.refreshToken());

        //2. Tạo Access Token mới
        String accessToken = jwtProvider.generateAccessToken(refreshToken.getCustomerAccount().getEmail(),
                refreshToken.getCustomerAccount().getUuid());

        return new LoginAccountResponse(accessToken, refreshToken.getToken(), "Bearer");
    }

    /** Kiểm tra email có tồn tại không?
    * @return {@code true} tài khoản có thể đăng ký được
     * @throws Exception Tài khoản bị trùng
    */
    @Override
    public boolean checkEmail(CheckEmailRequest request) {
        authValidator.checkEmail(request.email());
        return true;
    }


}
