package com.shop.inventoryservice.entity;

import com.shop.inventoryservice.common.entity.BaseLongEntity;
import com.shop.inventoryservice.common.enums.ReservationStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "inventorie_reservations")
public class InventoryReservation extends BaseLongEntity {

    private String orderCode;
    private Long productId;
    private Integer quantity;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.RESERVED;
}
