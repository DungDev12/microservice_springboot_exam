package com.shop.orderservice.client.grpc;

import com.shop.grpc.inventory.*;
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

    public ExportInventoryGrpcResponse exportInventory(ExportInventoryRequest request){
        try {
            return inventoryBlockingStub
                    .withDeadlineAfter(60, TimeUnit.SECONDS) // set thời gian chết
                    .exportProduct(request);
        } catch (StatusRuntimeException ex) {
            // Demo: ném lại để tầng gọi xử lý.
            // Production: chuyển status thành exception phù hợp.
            throw ex;
        }
    }

    public RestoreInventoryGrpcResponse restoreInventory(RestoreInventoryRequest request){
        try{
            return inventoryBlockingStub
                    .withDeadlineAfter(60, TimeUnit.SECONDS) // set thời gian chết
                    .restoreProduct(request);
        }catch (StatusRuntimeException ex) {
            // Demo: ném lại để tầng gọi xử lý.
            // Production: chuyển status thành exception phù hợp.
            throw ex;
        }
    }

    public ReservationInventoryGrpcResponse reservationInventory(ReservationInventoryRequest request){
        try{
            return inventoryBlockingStub
                    .withDeadlineAfter(60, TimeUnit.SECONDS) // set thời gian chết
                    .reservationProduct(request);
        }catch (StatusRuntimeException ex) {
            // Demo: ném lại để tầng gọi xử lý.
            // Production: chuyển status thành exception phù hợp.
            throw ex;
        }
    }
}
