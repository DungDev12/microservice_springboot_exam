package com.shop.inventoryservice.repository;

import com.shop.inventoryservice.common.enums.ReservationStatus;
import com.shop.inventoryservice.entity.InventoryReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Long> {
    List<InventoryReservation> findAllByOrderCodeAndStatus(String orderCode, ReservationStatus status);

    @Modifying
    @Query("""
        UPDATE InventoryReservation ir
        SET ir.status = :status
        WHERE ir.orderCode = :orderCode AND ir.status = "RESERVED"
    """)
    void updateStatusByOrderCode(
            @Param("orderCode") String orderCode,
            @Param("status") ReservationStatus status
    );
}
