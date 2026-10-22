package com.shop.identitycustomerservice.entity;

import com.shop.identitycustomerservice.common.entity.BaseUuidEntity;
import com.shop.identitycustomerservice.common.enums.StatusAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customer_accounts")
public class CustomerAccount extends BaseUuidEntity {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(
            regexp = "^0\\d{9}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phoneNumber; // 0765775120

    @NotBlank(message = "Mật khẩu không được để trống")
    private String passwordHash;

    @Column(name = "is_active") // Tài khoản được kích hoạt hay không
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    private StatusAccount status = StatusAccount.USED;
}
