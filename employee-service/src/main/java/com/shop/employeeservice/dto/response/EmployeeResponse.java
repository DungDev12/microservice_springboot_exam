package com.shop.employeeservice.dto.response;

import java.time.LocalDate;

public record EmployeeResponse(
        String employeeCode,
        String firstName,
        String lastName,
        String phoneNumber,
        LocalDate dateOfBirth,
        String address
) {
}
