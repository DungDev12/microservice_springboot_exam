package com.shop.inventoryservice.service;

import com.shop.inventoryservice.common.enums.ReservationStatus;
import com.shop.inventoryservice.entity.InventoryReservation;
import com.shop.inventoryservice.repository.InventoryReservationRepository;
import com.shop.inventoryservice.service.interfaces.IInventoryReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryReservationService implements IInventoryReservationService {

    private final InventoryReservationRepository inventoryReservationRepository;

    @Override
    public List<InventoryReservation> insertInventoryReservations(List<InventoryReservation> inventoryReservations) {
        return inventoryReservationRepository.saveAll(inventoryReservations);
    }

    @Override
    public List<InventoryReservation> findAllInventoryReservationsByOrderCode(String orderCode) {
        return inventoryReservationRepository.findAllByOrderCodeAndStatus(orderCode, ReservationStatus.RESERVED);
    }

    @Override
    public void updateAllStatusExportedByOrderCode(String orderCode) {
        inventoryReservationRepository.updateStatusByOrderCode(
                orderCode,
                ReservationStatus.EXPORT
        );
    }
}
