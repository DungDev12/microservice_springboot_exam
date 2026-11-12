package com.shop.customerservice.dto.response;

import java.time.LocalDate;

public record CustomerResponse(
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate dateOfBirth
) {
}
