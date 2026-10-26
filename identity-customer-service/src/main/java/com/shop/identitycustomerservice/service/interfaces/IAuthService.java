package com.shop.identitycustomerservice.service.interfaces;

import com.shop.identitycustomerservice.dto.request.*;
import com.shop.identitycustomerservice.dto.response.*;

public interface IAuthService {
    RegisterAccountResponse registerAccount(RegisterAccountRequest request);
    LoginAccountResponse loginAccount(LoginAccountRequest request);
    LoginAccountResponse refreshAccount(RefreshTokenRequest request);
    void changePassword(ChangePasswordRequest request);

    OtpVerifyResponse otpVerifyNewAccount(OtpVerifyRequest request);
    void resendVerificationCode(String email);
    boolean checkEmail(CheckEmailRequest request);

    ResetPasswordOTPResponse resetPasswordOTP(ResetPasswordOTPRequest request);
    VerifyResetPasswordOTPResponse verifyOtpForResetPassword(OtpVerifyRequest request);
    boolean confirmResetPasswordOtp(ConfirmResetPasswordRequest request);
}
