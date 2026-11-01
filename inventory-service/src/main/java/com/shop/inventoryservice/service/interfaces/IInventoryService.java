package com.shop.inventoryservice.service.interfaces;

import com.shop.grpc.shared.CreateInventoryRequest;
import com.shop.grpc.shared.CreateInventoryResponse;
import com.shop.inventoryservice.dto.request.UpdateInventoryRequest;
import com.shop.inventoryservice.dto.response.InventoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInventoryService {

    CreateInventoryResponse createInventory(CreateInventoryRequest request);

    InventoryResponse updateInventory(Long id, UpdateInventoryRequest request);

    Page<InventoryResponse> findAll(Pageable pageable);
}
