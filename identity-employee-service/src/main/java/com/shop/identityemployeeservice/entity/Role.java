package com.shop.identityemployeeservice.entity;

import com.shop.identityemployeeservice.common.entity.BaseLongEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "roles")
public class Role extends BaseLongEntity {

    @NotBlank(message = "Tên quyền không được để trống")
    private String name;
    private String description;

    @Builder.Default
    private boolean active = true;

    @NotNull(message = "Người tạo không được để trống")
    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();
}
