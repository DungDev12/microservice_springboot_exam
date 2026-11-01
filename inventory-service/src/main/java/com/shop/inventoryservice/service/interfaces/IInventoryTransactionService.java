package com.shop.inventoryservice.service.interfaces;

import com.shop.inventoryservice.dto.response.InventoryTransactionsResponse;
import com.shop.inventoryservice.entity.Inventory;
import com.shop.inventoryservice.entity.InventoryTransaction;

import java.util.List;

public interface IInventoryTransactionService {

    void createInventoryTransaction(Inventory inventory,int quantity);

    void insertInventoryTransaction(
            Inventory inventory,
            int oldQuantity,
            String type,
            String reference,
            String referenceType,
            String note);

    void updateInventoryTransaction(
            Inventory inventory,
            int oldQuantity,
            String type,
            String reference,
            String referenceType,
            String note
    );

    List<InventoryTransactionsResponse> getAllByInventoryId(Long inventoryId);
}
