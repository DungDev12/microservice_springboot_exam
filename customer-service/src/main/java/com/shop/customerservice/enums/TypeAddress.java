package com.shop.customerservice.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeAddress {
    HOME("Nhà"),
    COMPANY("Công ty");

    private final String value;
}
