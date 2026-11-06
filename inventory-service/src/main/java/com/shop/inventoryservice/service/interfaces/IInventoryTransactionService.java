package com.shop.inventoryservice.service.interfaces;

import com.shop.inventoryservice.common.enums.TransactionType;
import com.shop.inventoryservice.dto.response.InventoryTransactionsResponse;
import com.shop.inventoryservice.entity.Inventory;
import com.shop.inventoryservice.entity.InventoryTransaction;

import java.util.List;

public interface IInventoryTransactionService {

    void createInventoryTransaction(Inventory inventory,int quantity);

    void insertInventoryTransaction(
          InventoryTransaction inventoryTransaction);

    void deleteInventoryTransactions(List<Long> listId);

    void deleteById(Long id);

    List<InventoryTransactionsResponse> getAllByInventoryId(Long inventoryId);
}
