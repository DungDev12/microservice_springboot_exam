package com.shop.inventoryservice.service;

import com.shop.grpc.inventory.*;

import com.shop.inventoryservice.common.enums.TransactionType;
import com.shop.inventoryservice.dto.request.UpdateInventoryRequest;
import com.shop.inventoryservice.dto.response.InventoryResponse;
import com.shop.inventoryservice.entity.Inventory;
import com.shop.inventoryservice.entity.InventoryReservation;
import com.shop.inventoryservice.entity.InventoryTransaction;
import com.shop.inventoryservice.exception.NotFoundException;
import com.shop.inventoryservice.mapper.InventoryMapper;
import com.shop.inventoryservice.repository.InventoryRepository;
import com.shop.inventoryservice.repository.InventoryReservationRepository;
import com.shop.inventoryservice.repository.InventoryTransactionRepository;
import com.shop.inventoryservice.service.interfaces.IInventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper  inventoryMapper;
    private final InventoryTransactionService inventoryTransactionService;
    private final InventoryReservationService inventoryReservationService;
    private final InventoryReservationRepository inventoryReservationRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;

    private String generateAutoReference(String prefix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = new Random().nextInt(900) + 100; // Số ngẫu nhiên từ 100-999
        return prefix + "-" + timestamp + "-" + randomNum;
    }

    @Transactional
    @Override
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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ExportInventoryGrpcResponse exportInventory(ExportInventoryRequest request) {
        List<Long> productIds = request.getListProductsList()
                .stream()
                .map(ListProducts::getProductId)
                .distinct()
                .toList();

        Map<Long, Integer> requestedQuantities = request.getListProductsList().stream()
                .collect(Collectors.groupingBy(
                        ListProducts::getProductId,
                        Collectors.summingInt(ListProducts::getQuantity)
                ));


        List<Inventory> inventoryProducts = inventoryRepository.findAllByProductIdIn(productIds);

        if (inventoryProducts.size() != productIds.size()) {
            return ExportInventoryGrpcResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Có sản phẩm trong danh sách không tồn tại trong hệ thống kho.")
                    .build();
        }

        List<InventoryTransaction> inventoryTransactions = new ArrayList<>();
        for (Inventory inventory : inventoryProducts) {
            int requestedQty = requestedQuantities.get(inventory.getProductId());
            int beforeQuantity = inventory.getQuantity();
            int afterQuantity = beforeQuantity - requestedQty;

            int beforeReserved = inventory.getReservedQuantity();
            int afterReserved = beforeReserved - requestedQty;

            inventory.setQuantity(afterQuantity);
            inventory.setReservedQuantity(afterReserved);
            inventoryTransactions.add(
                    InventoryTransaction.builder()
                            .inventoryId(inventory)
                            .type(TransactionType.EXPORT_SALE)
                            .referenceType("XUẤT HÀNG")
                            .reference(generateAutoReference(request.getOrderCode()))
                            .beforeChange(beforeQuantity)
                            .afterChange(afterQuantity)
                            .quantityChange(-requestedQty)
                            .note(String.format("Xuất hàng cho đơn hàng %s", request.getOrderCode()))
                            .build()
            );
        }
        inventoryRepository.saveAll(inventoryProducts);
        // Lưu lịch sử
        inventoryTransactionRepository.saveAll(inventoryTransactions);
        inventoryReservationService.updateAllStatusExportedByOrderCode(request.getOrderCode());
        // Gọi bên giao hàng để nhận hàng

        return ExportInventoryGrpcResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Xuất kho thành công.")
                .build();
    }


    // Khách hàng huỷ đơn
    @Override
    @Transactional
    public RestoreInventoryGrpcResponse restoreInventory(RestoreInventoryRequest request) {

        List<InventoryReservation> inventoryReservations = inventoryReservationService.findAllInventoryReservationsByOrderCode(request.getOrderCode());
        if (inventoryReservations.isEmpty()) {
            return RestoreInventoryGrpcResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Không tìm thấy reservation của đơn hàng.")
                    .build();
        }
        List<Long> productIds = inventoryReservations.stream()
                .map(InventoryReservation::getProductId)
                .distinct()
                .toList();

        List<Inventory> inventories =
                inventoryRepository.findAllByProductIdIn(productIds);

        Map<Long, Inventory> inventoryMap =
                inventories.stream()
                        .collect(Collectors.toMap(
                                Inventory::getProductId,
                                Function.identity()
                        ));

        for (InventoryReservation  inventoryReservation : inventoryReservations) {
            Inventory inventory =
                    inventoryMap.get(inventoryReservation.getProductId());

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity()
                            - inventoryReservation.getQuantity()
            );
        }

        inventoryRepository.saveAll(inventories);
        inventoryReservationRepository.deleteAll(inventoryReservations);

        return RestoreInventoryGrpcResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Huỷ đơn hàng thành công")
                .build();
    }


    // Xử lý tạm để giữ hàng cho khách
    @Override
    @Transactional
    public ReservationInventoryGrpcResponse reservationInventory(ReservationInventoryRequest request) {
        List<Long> productIds = request.getListProductsList()
                .stream()
                .map(ListProducts::getProductId)
                .distinct()
                .toList();
        List<Inventory> inventoryProducts = inventoryRepository.findAllByProductIdIn(productIds);

        if (inventoryProducts.size() != productIds.size()) {
            return ReservationInventoryGrpcResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Có sản phẩm trong danh sách không tồn tại trong hệ thống kho.")
                    .build();
        }


        Map<Long, Integer> requestedQuantities = request.getListProductsList().stream()
                .collect(Collectors.groupingBy(
                        ListProducts::getProductId,
                        Collectors.summingInt(ListProducts::getQuantity)
                ));

        for (Inventory inventory : inventoryProducts) {
            int currentAvailable = inventory.getAvailableQuantity();
            int requestedQty = requestedQuantities.getOrDefault(inventory.getProductId(), 0);

            // Nếu số lượng khả dụng trừ đi số lượng mua mà bé hơn 0 -> Báo lỗi lập tức
            if (currentAvailable - requestedQty < 0) {
                System.out.println("Sản phẩm không đủ");
                // Ném lỗi gRPC chuẩn để báo về cho Order-Service biết chính xác lý do
                return ReservationInventoryGrpcResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage(String.format("Sản phẩm ID %d không đủ số lượng tồn kho khả dụng (Hiện có: %d, Cần: %d)",inventory.getProductId(), currentAvailable, requestedQty))
                        .build();
            }
        }
        List<InventoryReservation> inventoryReservations = new ArrayList<>();
        for (Inventory inventory : inventoryProducts) {
            inventory.setReservedQuantity(inventory.getReservedQuantity() + requestedQuantities.get(inventory.getProductId()));
            inventoryReservations.add(InventoryReservation.builder()
                            .orderCode(request.getOrderCode())
                            .quantity(requestedQuantities.get(inventory.getProductId()))
                            .productId(inventory.getProductId())
                    .build());
        }
        inventoryRepository.saveAll(inventoryProducts);
        inventoryReservationService.insertInventoryReservations(inventoryReservations);
        return ReservationInventoryGrpcResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Đã lưu hàng giữ để khách hàng thanh toán")
                .build();
    }


    private Inventory findById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy kho hàng"));
    }

    private boolean exitsByProductId(Long productId) {
        return inventoryRepository.existsByProductId(productId);
    }
}
