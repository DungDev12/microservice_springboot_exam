package com.shop.identitycustomerservice.client.grpc;

import com.shop.grpc.shared.CreateProfileRequest;
import com.shop.grpc.shared.CreateProfileResponse;
import com.shop.grpc.shared.CustomerGrpcServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.grpc.client.GrpcChannelFactory;

import java.util.concurrent.TimeUnit;

@Component
@Getter
@RequiredArgsConstructor
public class CustomerGrpcClient {

    private final CustomerGrpcServiceGrpc.CustomerGrpcServiceBlockingStub customerBlockingStub;

    public CreateProfileResponse createProfile(CreateProfileRequest request){
        try {
            return customerBlockingStub
                    .withDeadlineAfter(60, TimeUnit.SECONDS) // set thời gian chết
                    .createProfile(request);
        } catch (StatusRuntimeException ex) {
            // Demo: ném lại để tầng gọi xử lý.
            // Production: chuyển status thành exception phù hợp.
            throw ex;
        }
    }
}
