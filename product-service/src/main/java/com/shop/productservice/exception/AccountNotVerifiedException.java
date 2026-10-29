package com.shop.productservice.exception;

import lombok.Getter;

@Getter
public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException(String message) {
        super(message);
    }
}
