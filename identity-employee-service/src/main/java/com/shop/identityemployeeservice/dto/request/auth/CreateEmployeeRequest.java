package com.shop.identityemployeeservice.dto.request.auth;

import java.time.LocalDate;

public record CreateEmployeeRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        LocalDate dateOfBirth
) {
}
