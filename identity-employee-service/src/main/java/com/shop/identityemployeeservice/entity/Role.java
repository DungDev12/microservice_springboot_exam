package com.shop.identityemployeeservice.entity;

import com.shop.identityemployeeservice.common.entity.BaseLongEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

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

    @NotBlank(message = "Người tạo không được để trống")
    private UUID createdBy;
}
