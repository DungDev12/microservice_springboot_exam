package com.shop.identitycustomerservice.entity;

import com.shop.identitycustomerservice.common.entity.BaseUuidEntity;
import com.shop.identitycustomerservice.common.enums.StatusAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Mật khẩu không được để trống")
    private String passwordHash;

    @Column(name = "is_active") // Tài khoản được kích hoạt hay không
    private boolean isActive = true;

    @Enumerated(EnumType.STRING)
    private StatusAccount status = StatusAccount.USED;
}
