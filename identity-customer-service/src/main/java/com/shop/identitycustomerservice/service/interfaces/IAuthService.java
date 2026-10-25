package com.shop.identitycustomerservice.service.interfaces;

import com.shop.identitycustomerservice.dto.request.*;
import com.shop.identitycustomerservice.dto.response.LoginAccountResponse;
import com.shop.identitycustomerservice.dto.response.OtpVerifyResponse;
import com.shop.identitycustomerservice.dto.response.RegisterAccountResponse;
import com.shop.identitycustomerservice.dto.response.ResetPasswordOTPResponse;

public interface IAuthService {
    RegisterAccountResponse registerAccount(RegisterAccountRequest request);
    LoginAccountResponse loginAccount(LoginAccountRequest request);
    LoginAccountResponse refreshAccount(RefreshTokenRequest request);

    OtpVerifyResponse otpVerify(OtpVerifyRequest request);
    void resendVerificationCode(String email);
    boolean checkEmail(CheckEmailRequest request);

    ResetPasswordOTPResponse resetPasswordOTP(ResetPasswordOTPRequest request);
}
