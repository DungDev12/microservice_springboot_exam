package com.shop.inventoryservice.service;

import com.shop.grpc.shared.CreateInventoryRequest;

import com.shop.grpc.shared.InventoryGrpcResponse;
import com.shop.inventoryservice.dto.request.UpdateInventoryRequest;
import com.shop.inventoryservice.dto.response.InventoryResponse;
import com.shop.inventoryservice.entity.Inventory;
import com.shop.inventoryservice.entity.InventoryTransaction;
import com.shop.inventoryservice.exception.NotFoundException;
import com.shop.inventoryservice.mapper.InventoryMapper;
import com.shop.inventoryservice.repository.InventoryRepository;
import com.shop.inventoryservice.service.interfaces.IInventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper  inventoryMapper;
    private final InventoryTransactionService inventoryTransactionService;

    private String generateAutoReference(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = new Random().nextInt(900) + 100; // Số ngẫu nhiên từ 100-999
        return prefix + "-" + timestamp + "-" + randomNum;
    }

    @Override
    @Transactional
    public InventoryGrpcResponse createInventory(CreateInventoryRequest request) {
        if(this.exitsByProductId(request.getProductId())){
//            throw new AlreadyExistsException("Sản phẩm đã tồn tại");
            return InventoryGrpcResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(String.format("Sản phẩm id %s đã tồn tại", request.getProductId()))
                    .build();
        }
        Inventory inventory = Inventory.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();
        Inventory newInventory = inventoryRepository.save(inventory);

        inventoryTransactionService.createInventoryTransaction(newInventory,request.getQuantity());
        return InventoryGrpcResponse.newBuilder()
                .setSuccess(true)
                .setMessage(String.format("Tạo thành công với sản phẩm id %s", inventory.getProductId()))
                .build();
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(Long id, UpdateInventoryRequest request) {
        // Tìm kho hàng
        Inventory inventory = inventoryRepository.findByIdAndProductId(id,request.productId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm trong kho"));

        // Lấy số lượng cũ
        int oldQuantity = inventory.getQuantity();
        inventoryMapper.updateInventory(request,inventory);

        // Tạo lịch sử
        InventoryTransaction inventoryTransaction;
        switch (request.type()){
            case ADJUSTMENT_PLUS,ADJUSTMENT_MINUS -> {
                inventoryTransaction = InventoryTransaction.builder()
                        .inventoryId(inventory)
                        .type(request.type())
                        .referenceType(request.referenceType())
                        .reference(generateAutoReference(request.referenceType()))
                        .beforeChange(oldQuantity)
                        .afterChange(oldQuantity + inventory.getQuantity())
                        .quantityChange(inventory.getQuantity())
                        .note(request.note())
                        .build();
                inventory.setQuantity(inventory.getQuantity() + oldQuantity);
            }
            default -> inventoryTransaction = InventoryTransaction.builder()
                    .inventoryId(inventory)
                    .type(request.type())
                    .referenceType(request.referenceType())
                    .reference(generateAutoReference("UPD"))
                    .beforeChange(oldQuantity)
                    .afterChange(inventory.getQuantity())
                    .quantityChange(inventory.getQuantity())
                    .note(request.note())
                    .build();
        }

        inventoryTransactionService.insertInventoryTransaction(inventoryTransaction);
        return inventoryMapper.toResponse(inventory);
    }


    @Override
    public Page<InventoryResponse> findAll(Pageable pageable) {
        return inventoryRepository.findAllByIsDeletedFalse(pageable).map(inventoryMapper::toResponse);
    }

    @Override
    public void deleteInventory(Long id, String deletedBy) {
        Inventory inventory = this.findById(id);
        inventory.setDeleted(true);
        inventory.setDeleteBy(UUID.fromString(deletedBy));
        inventoryRepository.save(inventory);
    }


    private Inventory findById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy kho hàng"));
    }

    private boolean exitsByProductId(Long productId) {
        return inventoryRepository.existsByProductId(productId);
    }
}
