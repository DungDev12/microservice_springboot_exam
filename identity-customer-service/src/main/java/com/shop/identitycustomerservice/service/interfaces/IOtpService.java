package com.shop.identitycustomerservice.service.interfaces;

import com.shop.identitycustomerservice.dto.request.OtpVerifyRequest;
import com.shop.identitycustomerservice.entity.CustomerAccount;

public interface IOtpService {

    String generateOtp(String accountId);

    void generateOtpAndSendEmail(CustomerAccount account);

    boolean verifyOtp(String accountId, OtpVerifyRequest request);
}
