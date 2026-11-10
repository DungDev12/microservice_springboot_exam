package com.shop.identityemployeeservice.repository;

import com.shop.identityemployeeservice.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String name);

    List<Permission> findAllByIsDeletedFalse();
}
