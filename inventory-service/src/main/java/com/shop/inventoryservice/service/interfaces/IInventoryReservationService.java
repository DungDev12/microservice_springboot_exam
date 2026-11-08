package com.shop.inventoryservice.service.interfaces;

import com.shop.inventoryservice.entity.InventoryReservation;

import java.util.List;

public interface IInventoryReservationService {


    List<InventoryReservation> insertInventoryReservations(List<InventoryReservation> inventoryReservations);

    List<InventoryReservation> findAllInventoryReservationsByOrderCode(String orderCode);

    void updateAllStatusExportedByOrderCode(String orderCode);
}
