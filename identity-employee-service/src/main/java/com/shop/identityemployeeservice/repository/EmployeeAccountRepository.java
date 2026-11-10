package com.shop.identityemployeeservice.repository;

import com.shop.identityemployeeservice.entity.EmployeeAccount;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, UUID> {
    boolean existsByUsername(String username);

    Optional<EmployeeAccount> findByUsername(@NonNull String username);
}
