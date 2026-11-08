package com.shop.identitycustomerservice.service.interfaces;

import com.shop.identitycustomerservice.dto.response.AccountEmailResponse;

public interface IAccountService {

    AccountEmailResponse getEmailAccountByUuid(String uuid);
}
