package com.shop.identitycustomerservice.service;

import com.shop.identitycustomerservice.dto.response.AccountEmailResponse;
import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.exception.NotFoundException;
import com.shop.identitycustomerservice.repository.CustomerAccountRepository;
import com.shop.identitycustomerservice.service.interfaces.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final CustomerAccountRepository customerAccountRepository;

    @Override
    public AccountEmailResponse getEmailAccountByUuid(String uuid) {
        CustomerAccount account = customerAccountRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return new AccountEmailResponse(
                account.getEmail()
        );
    }
}
