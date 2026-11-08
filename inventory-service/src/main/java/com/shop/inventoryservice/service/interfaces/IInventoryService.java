package com.shop.inventoryservice.service.interfaces;

import com.shop.grpc.inventory.*;
import com.shop.inventoryservice.dto.request.UpdateInventoryRequest;
import com.shop.inventoryservice.dto.response.InventoryResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IInventoryService {


    @Transactional
    InventoryGrpcResponse createInventory(CreateInventoryRequest request);

    InventoryResponse updateInventory(Long id, UpdateInventoryRequest request);

    Page<InventoryResponse> findAll(Pageable pageable);

    void deleteInventory(Long id, String deletedBy);

    ExportInventoryGrpcResponse exportInventory(ExportInventoryRequest request);

    RestoreInventoryGrpcResponse restoreInventory(RestoreInventoryRequest request);

    ReservationInventoryGrpcResponse reservationInventory(ReservationInventoryRequest request);

}
