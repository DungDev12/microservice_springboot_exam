package com.shop.identitycustomerservice.service.interfaces;

import com.shop.identitycustomerservice.dto.request.RefreshTokenRequest;
import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.entity.RefreshToken;

public interface IRefreshTokenService {

    RefreshToken createRefreshToken(CustomerAccount account);

    RefreshToken findByRefreshToken(String refreshToken);

    void deleteAllRefreshTokens(CustomerAccount account);
    void deleteRefreshToken(Long id, RefreshToken refreshToken);
}
