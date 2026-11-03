package com.shop.inventoryservice.service;

import com.shop.inventoryservice.common.enums.TransactionType;
import com.shop.inventoryservice.dto.response.InventoryTransactionsResponse;
import com.shop.inventoryservice.entity.Inventory;
import com.shop.inventoryservice.entity.InventoryTransaction;
import com.shop.inventoryservice.mapper.InventoryTransactionsMapper;
import com.shop.inventoryservice.repository.InventoryTransactionRepository;
import com.shop.inventoryservice.service.interfaces.IInventoryTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryTransactionService implements IInventoryTransactionService {

    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final InventoryTransactionsMapper inventoryTransactionsMapper;

    @Override
    @Transactional
    public void createInventoryTransaction(Inventory inventory, int quantity) {
        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .inventoryId(inventory)
                .quantityChange(quantity)
                .beforeChange(0)
                .afterChange(quantity)
                .type(TransactionType.AWAITING_STOCK_ARRIVAL) // Chờ nhập hàng
                .referenceType("CHỜ NHẬP HÀNG")
                .note("Sản phẩm mới được tạo và nhập kho")
                .build();
        inventoryTransactionRepository.save(inventoryTransaction);
    }

    @Override
    public void insertInventoryTransaction(
         InventoryTransaction inventoryTransaction) {
        inventoryTransactionRepository.save(inventoryTransaction);
    }


    @Override
    public void deleteInventoryTransactions(List<Long> listId) {
        listId.forEach(this::deleteById);
    }

    @Override
    public void deleteById(Long id) {
        inventoryTransactionRepository.deleteById(id);
    }

    @Override
    public List<InventoryTransactionsResponse> getAllByInventoryId(Long inventoryId) {
        return inventoryTransactionRepository.findAllByInventoryId_Id(inventoryId)
                .stream().map(inventoryTransactionsMapper::toResponse).toList();

    }

}
