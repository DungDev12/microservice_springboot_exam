package com.shop.inventoryservice.repository;

import com.shop.inventoryservice.common.enums.ReservationStatus;
import com.shop.inventoryservice.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    boolean existsByProductId(Long productId);

    Optional<Inventory> findByProductId(Long productId);

    Page<Inventory> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Inventory> findByIdAndProductId(Long id, Long productId);

    List<Inventory> findAllByProductIdIn(Collection<Long> productIds);


}
