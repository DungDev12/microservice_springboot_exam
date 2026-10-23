package com.shop.identitycustomerservice.repository;

import com.shop.identitycustomerservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteAllByCustomerAccountUuid(UUID customerAccountUuid);

    void deleteByIdAndCustomerAccountUuid(Long id, UUID customerAccountUuid);

    Optional<RefreshToken> findByToken(String token);
}
