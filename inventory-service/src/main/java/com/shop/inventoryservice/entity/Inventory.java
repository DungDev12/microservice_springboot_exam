package com.shop.inventoryservice.entity;

import com.shop.inventoryservice.common.entity.BaseLongEntity;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Inventory extends BaseLongEntity {

    private Long productId;

    private int quantity = 0;
    private int reservedQuantity;

    public int getAvailableQuantity(){
        return quantity - reservedQuantity;
    }
}
