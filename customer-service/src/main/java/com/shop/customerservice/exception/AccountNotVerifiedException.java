package com.shop.customerservice.exception;

import lombok.Getter;

@Getter
public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException(String message) {
        super(message);
    }
}
