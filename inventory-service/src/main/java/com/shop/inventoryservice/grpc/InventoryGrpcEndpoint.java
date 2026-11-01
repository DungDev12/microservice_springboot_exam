package com.shop.inventoryservice.grpc;


import com.shop.grpc.shared.CreateInventoryRequest;
import com.shop.grpc.shared.CreateInventoryResponse;
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
//    private final CustomerGrpcValidation customerGrpcValidation;
//

    @Override
    public void createInventory(CreateInventoryRequest request,
                                StreamObserver<CreateInventoryResponse> responseObserver) {
        try{

            CreateInventoryResponse response = inventoryService.createInventory(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex){
            responseObserver.onError(Status.ALREADY_EXISTS
                            .withDescription("Không thể tạo hồ sơ khách")
                            .asRuntimeException());
        }

    }


//    @Override
//    public void createProfile(
//            CreateProfileRequest request,
//            StreamObserver<CreateProfileResponse> responseObserver
//    ) {
//        try {
//            customerGrpcValidation.validateRequest(request);
//
//            customerService.createProfile(request);
//
//            CreateProfileResponse response =
//                    CreateProfileResponse.newBuilder()
//                            .setSuccess(true)
//                            .setMessage("Tạo hồ sơ khách hàng thành công")
//                            .build();
//
//            responseObserver.onNext(response);
//            responseObserver.onCompleted();
//
//        } catch (Exception ex) {
//            responseObserver.onError(
//                    Status.ALREADY_EXISTS
//                            .withDescription("Không thể tạo hồ sơ khách")
//                            .asRuntimeException()
//            );
//        }
//    }

}
