package com.shop.identityemployeeservice.repository;

import com.shop.identityemployeeservice.entity.EmployeeAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, UUID> {
}
