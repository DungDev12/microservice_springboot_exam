package com.shop.inventoryservice.service;

import com.shop.grpc.shared.CreateInventoryRequest;
import com.shop.grpc.shared.CreateInventoryResponse;
import com.shop.inventoryservice.common.enums.TransactionType;
import com.shop.inventoryservice.dto.request.UpdateInventoryRequest;
import com.shop.inventoryservice.dto.response.InventoryResponse;
import com.shop.inventoryservice.entity.Inventory;
import com.shop.inventoryservice.entity.InventoryTransaction;
import com.shop.inventoryservice.exception.AlreadyExistsException;
import com.shop.inventoryservice.exception.NotFoundException;
import com.shop.inventoryservice.mapper.InventoryMapper;
import com.shop.inventoryservice.repository.InventoryRepository;
import com.shop.inventoryservice.service.interfaces.IInventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper  inventoryMapper;
    private final InventoryTransactionService inventoryTransactionService;

    @Override
    @Transactional
    public CreateInventoryResponse createInventory(CreateInventoryRequest request) {
        if(this.exitsByProductId(request.getProductId())){
//            throw new AlreadyExistsException("Sản phẩm đã tồn tại");
            return CreateInventoryResponse.newBuilder()
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
        return CreateInventoryResponse.newBuilder()
                .setSuccess(true)
                .setMessage(String.format("Tạo thành công với sản phẩm id %s", inventory.getProductId()))
                .build();
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(Long id, UpdateInventoryRequest request) {
        // Tìm kho hàng
        Inventory inventory = inventoryRepository.findByProductId(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm trong kho"));

        // Lấy số lượng cũ
        int oldQuantity = inventory.getQuantity();
        inventoryMapper.updateInventory(request,inventory);
        inventoryTransactionService.updateInventoryTransaction(
                inventory,
                oldQuantity,
                request.type(),
                null,
                "UPDATE",
                request.note());
        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public Page<InventoryResponse> findAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable)
                .map(inventoryMapper::toResponse);
    }


    private Inventory findById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy kho hàng"));
    }

    private boolean exitsByProductId(Long productId) {
        return inventoryRepository.existsByProductId(productId);
    }
}
