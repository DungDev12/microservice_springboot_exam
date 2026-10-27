package com.shop.identitycustomerservice.service;

import com.shop.grpc.shared.CreateProfileRequest;
import com.shop.grpc.shared.CreateProfileResponse;
import com.shop.identitycustomerservice.client.grpc.CustomerGrpcClient;
import com.shop.identitycustomerservice.common.enums.StatusAccount;
import com.shop.identitycustomerservice.config.jwt.JwtProvider;
import com.shop.identitycustomerservice.dto.request.*;
import com.shop.identitycustomerservice.dto.response.*;
import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.entity.RefreshToken;
import com.shop.identitycustomerservice.event.NotificationEvent;
import com.shop.identitycustomerservice.event.NotificationEventProducer;
import com.shop.identitycustomerservice.exception.AccessDeniedException;
import com.shop.identitycustomerservice.exception.InternalServerException;
import com.shop.identitycustomerservice.exception.NotFoundException;
import com.shop.identitycustomerservice.repository.CustomerAccountRepository;
import com.shop.identitycustomerservice.security.CurrentUserService;
import com.shop.identitycustomerservice.security.UserPrincipal;
import com.shop.identitycustomerservice.service.interfaces.IAuthService;
import com.shop.identitycustomerservice.validator.AuthValidator;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final CustomerAccountRepository customerAccountRepository;
    private final NotificationEventProducer notificationEventProducer;
    private final RefreshTokenService  refreshTokenService;
    private final AuthValidator authValidator;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomerGrpcClient customerGrpcClient;
    private final OtpService otpService;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
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

        //4. Tạo thông tin khách hàng
        CreateProfileResponse customerProfile = customerGrpcClient.createProfile(CreateProfileRequest.newBuilder()
                .setAccountId(customerAccount.getUuid().toString())
                        .setFirstName(request.info().firstName())
                        .setLastName(request.info().lastName())
                        .setDateBirth(request.info().dateBirth().toString())
                        .setPhoneNumber(request.info().phoneNumber())
                        .setGender(request.info().gender().toString())
                .build());

        if(!customerProfile.getSuccess()){
            throw new InternalServerException("Tạo thất bại");
        }

        //5. Tạo mã OTP và Gửi OTP qua email xác thực tài khoản

        otpService.generateOtpAndSendEmail(customerAccount);

        //6. Tạo accessToken và RefreshToken
        String accessToken = jwtProvider.generateAccessToken(customerAccount.getEmail(),customerAccount.getUuid());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(customerAccount);
        //7. Trả đến trang login
        return new RegisterAccountResponse(
                true,
                "/auth/login",
                "VERIFY_ACCOUNT",
                customerAccount.getEmail(),
                accessToken,
                refreshToken.getToken());
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

        return new LoginAccountResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer");
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        // Tìm tài khoản bằng và đổi từ UserPrincipal
        CustomerAccount account = customerAccountRepository
                .findByUuid(currentUserService.getCurrentUser().getId())
                .orElseThrow(() -> new AccessDeniedException(
                        "Không tìm thấy tài khoản",
                        HttpStatus.UNAUTHORIZED
                ));
        authValidator.changePasswordValidator(request, account);

        account.setPasswordHash(passwordEncoder.encode(request.password()));

        customerAccountRepository.save(account);
    }

    @Override
    public OtpVerifyResponse otpVerifyNewAccount(OtpVerifyRequest request) {
        Map<String, String> jwtParse = jwtProvider.getUuidFromToken(request.token());
        boolean matches = otpService.verifyOtp(jwtParse, request);
        if(!matches) {
            throw new AccessDeniedException("Mã OTP không khớp", HttpStatus.UNAUTHORIZED);
        }

        // Xác thực thành cồn thì gửi mail thông báo chào mừng người mới
        notificationEventProducer.send(
                new NotificationEvent(
                        UUID.fromString(jwtParse.get("uuid")),
                        jwtParse.get("email")
                ),
                "hello-new-customer-events"
        );

        return new OtpVerifyResponse(
                true,
                "Người dùng xác thực thành công"
        );
    }

    @Override
    public void resendVerificationCode(String email) {
        CustomerAccount customerAccount = customerAccountRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Email không tồn tại", HttpStatus.UNAUTHORIZED));

        otpService.generateOtpAndSendEmail(customerAccount);
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

    @Override
    public ResetPasswordOTPResponse resetPasswordOTP(ResetPasswordOTPRequest request) {
        // Kiểm tra email có tồn tại không ?
        CustomerAccount customerAccount = customerAccountRepository.findByEmail(request.email())
                .orElseThrow(() -> new AccessDeniedException("Email không tồn tại", HttpStatus.UNAUTHORIZED));

        // Tạo resetToken duy trì trong thời gian ngắn
        String resetToken =  jwtProvider.generateToken(customerAccount.getEmail(),customerAccount.getUuid());

        // Nếu tồn tại gửi OTP qua email tài khoản
        otpService.generateOtpAndSendEmail(customerAccount);
        return new ResetPasswordOTPResponse(
                true,
                "/password/reset/verify-otp",
                "VERIFY-OTP",
                resetToken
        );
    }

    @Override
    public VerifyResetPasswordOTPResponse verifyOtpForResetPassword(OtpVerifyRequest request) {

        // Kiểm tra resetToken hợp lệ không
        Map<String, String> jwtParse = jwtProvider.getUuidFromToken(request.token());
        boolean matches = otpService.verifyOtp(jwtParse, request);
        if(!matches) {
            throw new AccessDeniedException("Mã OTP không khớp", HttpStatus.UNAUTHORIZED);
        }

        // Tạo resetToken duy trì trong thời gian ngắn và kèm theo quyền đổi mật khẩu
        String resetToken =  jwtProvider.generatePasswordResetToken(jwtParse.get("email"), UUID.fromString(jwtParse.get("uuid")));

        return new VerifyResetPasswordOTPResponse(
                true,
                "/password/reset/confirm",
                "CHANGE_PASSWORD",
                resetToken
        );
    }

    @Override
    public boolean confirmResetPasswordOtp(ConfirmResetPasswordRequest request) {
        Map<String, String> jwtParse = jwtProvider.getPermissionResetPassword(request.token());

        if(!jwtParse.get("permission").equals("PASSWORD_RESET")) {
            throw new AccessDeniedException("Bạn không có quyền khôi phục mật khẩu", HttpStatus.FORBIDDEN);
        }

        CustomerAccount account = customerAccountRepository.findByUuid(
                UUID.fromString(jwtParse.get("uuid"))
        ).orElseThrow(() -> new NotFoundException("Không tìm thấy"));

        account.setPasswordHash(
                passwordEncoder.encode(request.password())
        );

        customerAccountRepository.save(account);
        return true;
    }


}
