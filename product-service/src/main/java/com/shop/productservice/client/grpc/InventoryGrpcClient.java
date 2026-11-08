package com.shop.productservice.client.grpc;

import com.shop.grpc.inventory.CreateInventoryRequest;
import com.shop.grpc.inventory.InventoryGrpcResponse;
import com.shop.grpc.inventory.InventoryGrpcServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Getter
@RequiredArgsConstructor
public class InventoryGrpcClient extends InventoryGrpcServiceGrpc.InventoryGrpcServiceImplBase {

    private final InventoryGrpcServiceGrpc.InventoryGrpcServiceBlockingStub inventoryBlockingStub;

    public InventoryGrpcResponse createInventory(CreateInventoryRequest request){
        try {
            return inventoryBlockingStub
                    .withDeadlineAfter(60, TimeUnit.SECONDS) // set thời gian chết
                    .createInventory(request);
        } catch (StatusRuntimeException ex) {
            // Demo: ném lại để tầng gọi xử lý.
            // Production: chuyển status thành exception phù hợp.
            throw ex;
        }
    }

}
