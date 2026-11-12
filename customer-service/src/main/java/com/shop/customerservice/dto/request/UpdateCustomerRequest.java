package com.shop.customerservice.dto.request;

import java.time.LocalDate;

public record UpdateCustomerRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate dateOfBirth
) {
}
