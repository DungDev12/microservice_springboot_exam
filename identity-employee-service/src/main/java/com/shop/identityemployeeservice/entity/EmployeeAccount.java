package com.shop.identityemployeeservice.entity;

import com.shop.identityemployeeservice.common.entity.BaseUuidEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employee_accounts")
public class EmployeeAccount extends BaseUuidEntity {

    private String username;
    private String passwordHash;

    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "employeeAccount")
    private List<RefreshToken> refreshTokens;
}
