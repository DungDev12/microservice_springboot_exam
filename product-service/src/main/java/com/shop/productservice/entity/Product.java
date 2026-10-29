package com.shop.productservice.entity;

import com.shop.productservice.common.entity.BaseLongEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@SuperBuilder
public class Product extends BaseLongEntity {

    @NotNull
    private String name; // Tên sản phẩm

    private String description; // Mô tả sản phẩm
    private boolean promotion; // Khuyến mãi
    private BigDecimal price; // Giá bán hiện tại
    private int discount; // Giảm giá

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // Thể loại cho phân loại sản phẩm

    @NotNull
    private UUID createBy;
}
