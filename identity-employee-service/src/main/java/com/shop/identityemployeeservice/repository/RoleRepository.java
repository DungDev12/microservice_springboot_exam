package com.shop.identityemployeeservice.repository;

import com.shop.identityemployeeservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);

    List<Role> findAllByIsDeletedFalse();

    Optional<Role> findByName(String name);
}
