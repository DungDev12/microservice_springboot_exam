package com.shop.inventoryservice.repository;

import com.shop.inventoryservice.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    List<InventoryTransaction> findAllByInventoryId_Id(Long inventoryIdId);
}
