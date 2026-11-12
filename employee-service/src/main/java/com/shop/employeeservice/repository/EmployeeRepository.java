package com.shop.employeeservice.repository;

import com.shop.employeeservice.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    @Query("""
        SELECT MAX(CAST(SUBSTRING(e.employeeCode, 3) AS Long))
        FROM Employee e
    """)
    Long findMaxEmployeeNumber();
}
