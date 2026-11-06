package com.shop.inventoryservice.entity;

import com.shop.inventoryservice.common.entity.BaseLongEntity;
import com.shop.inventoryservice.common.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "inventory_transactions")
public class InventoryTransaction extends BaseLongEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventoryId; // kho ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type; // Loại giao dịch

    @Column(nullable = false)
    private int quantityChange; // Số lượng thay đổi

    private int beforeChange; // Số lượng trước thi thay đổi

    private int afterChange; // Số lượng sau thi thay đổi

    private String reference; // Mã tham chiếu
    private String referenceType; // Loại tham chiếu

    @Column(columnDefinition = "TEXT")
    private String note;
}
