package com.shop.identityemployeeservice.entity;

import com.shop.identityemployeeservice.common.entity.BaseUuidEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "employee_account_roles",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
