package com.shop.customerservice.grpc;

import com.shop.customerservice.service.CustomerService;
import com.shop.customerservice.validation.CustomerGrpcValidation;
import com.shop.grpc.shared.CreateProfileRequest;
import com.shop.grpc.shared.CreateProfileResponse;
import com.shop.grpc.shared.CustomerGrpcServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CustomerGrpcEndpoint extends CustomerGrpcServiceGrpc.CustomerGrpcServiceImplBase {

    private final CustomerService customerService;
    private final CustomerGrpcValidation customerGrpcValidation;

    @Override
    public void createProfile(
            CreateProfileRequest request,
            StreamObserver<CreateProfileResponse> responseObserver
    ) {
        try {
            customerGrpcValidation.validateRequest(request);

            customerService.createProfile(request);

            CreateProfileResponse response =
                    CreateProfileResponse.newBuilder()
                            .setSuccess(true)
                            .setMessage("Tạo hồ sơ khách hàng thành công")
                            .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription("Không thể tạo hồ sơ khách")
                            .asRuntimeException()
            );
        }
    }

}
