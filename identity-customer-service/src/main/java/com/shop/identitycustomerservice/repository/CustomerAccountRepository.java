package com.shop.identitycustomerservice.repository;

import com.shop.identitycustomerservice.entity.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount,UUID> {
    boolean existsByEmailIgnoreCase(String email);
}
