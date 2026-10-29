package com.shop.productservice.entity;

import com.shop.productservice.common.entity.BaseLongEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "categories")
public class Category extends BaseLongEntity {

    @NotNull
    @Column(unique = true)
    private String name;
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @NotNull
    private UUID createBy;
}
