package com.shop.customerservice.dto.request;

import com.shop.customerservice.common.enums.Gender;
import com.shop.customerservice.entity.Address;

import java.time.LocalDate;

public record UpdateProfileRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        Gender gender,
        LocalDate dateBirth,
        Address address
) {
}
