package com.shop.inventoryservice.grpc;


import com.shop.grpc.inventory.*;
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

    @Override
    public void exportProduct(ExportInventoryRequest request,
                                StreamObserver<ExportInventoryGrpcResponse> responseStreamObserver){
        try{
            ExportInventoryGrpcResponse response = inventoryService.exportInventory(request);
            responseStreamObserver.onNext(response);
            responseStreamObserver.onCompleted();
        } catch (Exception ex){
            responseStreamObserver.onError(Status.ALREADY_EXISTS
                    .withDescription("Không thể tạo hồ sơ khách")
                    .asRuntimeException());
        }
    }


    @Override
    public void restoreProduct(RestoreInventoryRequest request, StreamObserver<RestoreInventoryGrpcResponse> responseStreamObserver){
        try{
            RestoreInventoryGrpcResponse response = inventoryService.restoreInventory(request);
            responseStreamObserver.onNext(response);
            responseStreamObserver.onCompleted();
        } catch (Exception ex){
            responseStreamObserver.onError(Status.ALREADY_EXISTS
                    .withDescription("Không thể tạo hồ sơ khách")
                    .asRuntimeException());
        }
    }

    @Override
    public void reservationProduct(ReservationInventoryRequest request, StreamObserver<ReservationInventoryGrpcResponse> responseStreamObserver){
        try{
            ReservationInventoryGrpcResponse response = inventoryService.reservationInventory(request);
            responseStreamObserver.onNext(response);
            responseStreamObserver.onCompleted();
        } catch (Exception ex){
            responseStreamObserver.onError(Status.ALREADY_EXISTS
                    .withDescription("Không thể tạo hồ sơ khách")
                    .asRuntimeException());
        }
    }
}
