package com.shop.identitycustomerservice.service;

import com.shop.identitycustomerservice.config.jwt.JwtProvider;
import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.entity.RefreshToken;
import com.shop.identitycustomerservice.exception.NotFoundException;
import com.shop.identitycustomerservice.repository.RefreshTokenRepository;
import com.shop.identitycustomerservice.service.interfaces.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(CustomerAccount account) {
        //Tạo token
        RefreshToken refreshToken = RefreshToken.builder()
                .customerAccount(account)
                .token(jwtProvider.generateRefreshToken(account.getEmail(),account.getUuid()))
                .expiryDate(Instant.now().plusMillis(jwtProvider.getJwtRefreshExpirationMs()))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy token"));
    }


    @Override
    public void deleteAllRefreshTokens(CustomerAccount account) {
        refreshTokenRepository.deleteAllByCustomerAccountUuid(account.getUuid());
    }

    @Override
    public void deleteRefreshToken(Long id, RefreshToken refreshToken) {
        refreshTokenRepository.deleteByIdAndCustomerAccountUuid(id, refreshToken.getCustomerAccount().getUuid());
    }


}
