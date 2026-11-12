package com.shop.identityemployeeservice.repository;

import com.shop.identityemployeeservice.entity.EmployeeAccount;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, UUID> {
    boolean existsByUsername(String username);

    Optional<EmployeeAccount> findByUsername(@NonNull String username);

    @Query("""
        SELECT DISTINCT p.name
        FROM EmployeeAccount e
        JOIN e.roles r
        JOIN r.permissions p
        WHERE e.uuid = :userUuid
          AND e.active = true
    """)
    Set<String> findPermissionsByUserUuid(
            @Param("userUuid") UUID userUuid
    );

    @Query("""
    SELECT MAX(r.version)
    FROM EmployeeAccount e
    JOIN e.roles r
    WHERE e.uuid = :userUuid
      AND e.active = true
""")
    Long findRoleVersionByUserUuid(
            @Param("userUuid") UUID userUuid
    );
}
