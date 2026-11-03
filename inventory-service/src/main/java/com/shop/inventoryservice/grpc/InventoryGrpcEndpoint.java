package com.shop.inventoryservice.grpc;


import com.shop.grpc.shared.CreateInventoryRequest;
import com.shop.grpc.shared.InventoryGrpcResponse;
import com.shop.grpc.shared.InventoryGrpcServiceGrpc;
import com.shop.inventoryservice.service.InventoryService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcEndpoint extends InventoryGrpcServiceGrpc.InventoryGrpcServiceImplBase {

    private final InventoryService inventoryService;

    @Override
    public void createInventory(CreateInventoryRequest request,
                                StreamObserver<InventoryGrpcResponse> responseObserver) {
        try{

            InventoryGrpcResponse response = inventoryService.createInventory(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex){
            responseObserver.onError(Status.ALREADY_EXISTS
                            .withDescription("Không thể tạo hồ sơ khách")
                            .asRuntimeException());
        }

    }

}
