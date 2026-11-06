package com.shop.inventoryservice.controller;

import com.shop.inventoryservice.dto.ApiResponse;
import com.shop.inventoryservice.dto.request.UpdateInventoryRequest;
import com.shop.inventoryservice.dto.response.InventoryResponse;
import com.shop.inventoryservice.service.InventoryService;
import com.shop.inventoryservice.service.InventoryTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryTransactionService inventoryTransactionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponse>>> getAllInventory(
            @PageableDefault(
                    page = 0,
                    value = 10,
                    direction = Sort.Direction.ASC,
                    sort = "id"
            )Pageable pageable
            ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.page(inventoryService.findAll(pageable),
                        "Lấy danh sách kho hàng thành công"));
    }

    @GetMapping("{inventoryId}/transactions")
    public ResponseEntity<ApiResponse<?>> getTransactionsByInventoryId(
            @PathVariable Long inventoryId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(inventoryTransactionService.getAllByInventoryId(inventoryId),
                        "Lấy chi tiết kho thành công",
                        HttpStatus.OK));
    }

    @PutMapping("{productId}/update")
    public ResponseEntity<ApiResponse<?>> updateInventory(
            @PathVariable Long productId,
            @RequestBody UpdateInventoryRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        inventoryService.updateInventory(productId,request),
                        "Cập nhật kho hàng thành công",
                        HttpStatus.OK
                ));
    }

    @DeleteMapping("transactions")
    public ResponseEntity<ApiResponse<?>> deleteInventoryTransactions(
            @RequestParam("ids") List<Long> ids
    ){
        inventoryTransactionService.deleteInventoryTransactions(ids);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        "Xoá lịch sử kho hàng thành công"
                ));
    }

    @DeleteMapping("{inventoryId}")
    public ResponseEntity<ApiResponse<?>> deleteInventoryTransactionById(
            @PathVariable Long inventoryId,
            @RequestHeader("X-Auth-Userid") String uuid
    ){
        inventoryService.deleteInventory(inventoryId, uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success("Xoá dữ liệu thành công"));
    }
}
