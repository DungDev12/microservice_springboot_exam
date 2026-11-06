package com.shop.orderservice.entity;

import com.shop.orderservice.common.entity.BaseLongEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
@SuperBuilder
public class OrderItem extends BaseLongEntity {

    private Long productId; // ID sản phẩm
    private String productName; // Tên sản phẩm
    private Long quantity; // số lượng đặt mua
    private BigDecimal price; // Giá tiền lúc mua
    private int discount; // Giảm giá ( nếu có )

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
