package com.shop.identitycustomerservice.entity;

import com.shop.identitycustomerservice.common.entity.BaseUuidEntity;
import com.shop.identitycustomerservice.common.enums.StatusAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StatusAccount status = StatusAccount.PENDING_VERIFICATION;

    private LocalDateTime verified_at;

    @OneToMany(mappedBy = "customerAccount")
    private List<RefreshToken> refreshTokens;
}
