package com.shop.identitycustomerservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException(String message) {
        super(message);
    }
}
