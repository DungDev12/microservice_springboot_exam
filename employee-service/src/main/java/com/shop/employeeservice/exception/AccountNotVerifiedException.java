package com.shop.employeeservice.exception;

import lombok.Getter;

@Getter
public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException(String message) {
        super(message);
    }
}
