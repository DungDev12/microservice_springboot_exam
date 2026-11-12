package com.shop.customerservice.dto.request;

public record AddressRequest(
        String fullName,
        String district,
        String street,
        String city,
        String zip,
        String country,
        String houseNumber,
        String phoneNumber,
        boolean isDefault
) {
}
