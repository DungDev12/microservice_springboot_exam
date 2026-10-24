package com.shop.identitycustomerservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.Period;

enum TypeAddress {
    HOME,
    COMPANY
}

public record RegisterAccountRequest(

        @Email(message = "Email không hợp lệ")
        @NotBlank(message = "Email không được để trống")
        String email,

        @NotBlank(message = "Mật khẩu không được để trống")
        @Length(min = 8,message = "Mật khẩu ít nhất 8 kí tự")
        String password,

        @NotNull(message = "Thông tin không được để trống")
        @Valid
        InfoCustomer info
) {
    public record InfoCustomer(

            @NotBlank(message = "Họ không được để trống")
            String firstName,

            @NotBlank(message = "Tên không được để trống")
            String lastName,

            @NotNull(message = "Ngày sinh không được để trống")
            LocalDate dateBirth,

            String phoneNumber
    ){
        @AssertTrue(message = "Bạn phải từ 16 tuổi trở lên")
        public boolean isOldEnough() {
            if (dateBirth == null) {
                return false;
            }
            return Period.between(dateBirth, LocalDate.now()).getYears() >= 16;
        }
    }
}
