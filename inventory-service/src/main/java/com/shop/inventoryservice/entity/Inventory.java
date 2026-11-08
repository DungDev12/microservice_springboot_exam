package com.shop.inventoryservice.entity;

import com.shop.inventoryservice.common.entity.BaseLongEntity;
import jakarta.persistence.Column;
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
@Table(name = "inventories")
public class Inventory extends BaseLongEntity {

    @Column(unique = true)
    private Long productId;

    @Builder.Default
    private Integer quantity = 0; // Số lượng hàng trong kho

    @Builder.Default
    private int reservedQuantity = 0; // Số lượng đang giữ hàng cho khách

    @OneToMany(mappedBy = "inventoryId")
    private List<InventoryTransaction> inventoryTransactions;

    // Số lượng khách có thể mua hàng
    public int getAvailableQuantity(){
        int total = (quantity != null) ? quantity : 0;
        return total - reservedQuantity;
    }
}
