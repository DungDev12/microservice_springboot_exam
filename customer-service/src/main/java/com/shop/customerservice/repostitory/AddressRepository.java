package com.shop.customerservice.repostitory;

import com.shop.customerservice.dto.response.CustomerAddressResponse;
import com.shop.customerservice.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, Long> {
    CustomerAddressResponse findAllByCustomerUuid(UUID customerUuid);

    Optional<Address> findByIdAndCustomer_Uuid(Long id, UUID customerUuid);

    @Modifying
    @Query("""
        UPDATE Address a
        SET a.isDefault = false
        WHERE a.customer.uuid = :customerId
          AND a.isDefault = true
    """)
    void resetDefaultAddress(
            @Param("customerId") UUID customerId
    );
}
