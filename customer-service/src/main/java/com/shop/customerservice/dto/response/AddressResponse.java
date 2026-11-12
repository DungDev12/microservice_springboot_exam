package com.shop.customerservice.dto.response;

public record AddressResponse(
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
