package com.shop.orderservice.repository;

import com.shop.orderservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> getAllByCustomerUuid(UUID customerUuid, Pageable pageable);

    Optional<Order> findOrderByCodeAndCustomerUuid(String code, UUID customerUuid);

    Order findOrderByCode(String code);
}
