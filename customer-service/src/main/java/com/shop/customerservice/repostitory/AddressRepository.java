package com.shop.customerservice.repostitory;

import com.shop.customerservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, Long> {
    void deleteByIdAndCustomer_Uuid(long id, UUID customerUuid);

    Optional<Address> findByIdAndCustomer_Uuid(long id, UUID customerUuid);

    @Modifying
    @Query("""
    UPDATE Address a
    SET a.isDefault = false
    WHERE a.customer.uuid = :userUuid
      AND a.isDefault = true
    """)
    int clearDefaultAddress(UUID userUuid);
}
