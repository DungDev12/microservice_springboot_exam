package com.shop.customerservice.repostitory;

import com.shop.customerservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByUuid(UUID uuid);
}
