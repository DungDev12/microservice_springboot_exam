package com.shop.inventoryservice.repository;

import com.shop.inventoryservice.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    boolean existsByProductId(Long productId);

    Optional<Inventory> findByProductId(Long productId);

    Page<Inventory> findAllByIsDeletedFalse(Pageable pageable);
}
