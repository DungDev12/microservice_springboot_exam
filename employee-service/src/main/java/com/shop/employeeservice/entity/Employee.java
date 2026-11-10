package com.shop.employeeservice.entity;

import com.shop.employeeservice.common.entity.BaseUuidEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employees")
public class Employee {
    private static final String CODE_PREFIX = "NV";

    @Id
    @Column(name = "account_id", nullable = false, updatable = false, unique = true)
    private UUID accountId;

    @Column(
            name = "employee_code",
            nullable = false,
            unique = true,
            updatable = false,
            length = 8
    )
    private String employeeCode;

    @NotBlank(message = "Họ không được để trống")
    private String firstName;

    @NotBlank(message = "Tên không được để trống")
    private String lastName;

    private String phoneNumber;

    private String address;

    private LocalDate dateOfBirth;


    private String getFullName(){
        return String.format("%s %s", firstName, lastName);
    }

    public void assignEmployeeCode(long number) {
        if (this.employeeCode != null) {
            throw new IllegalStateException("Mã nhân viên đã được cấp");
        }
        this.employeeCode = "%s%06d".formatted(CODE_PREFIX, number);
    }

}
