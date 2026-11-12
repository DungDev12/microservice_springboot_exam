package com.shop.identityemployeeservice.entity;

import com.shop.identityemployeeservice.common.entity.BaseLongEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "permissions")
public class Permission extends BaseLongEntity {

    @NotBlank(message = "Tên quyền không được để trống")
    private String name;
    private String description;

    @Builder.Default
    private boolean active = true;

    @NotNull(message = "Người tạo không được để trống")
    @Column(name = "created_by", nullable = false)
    private UUID createdBy;
}
