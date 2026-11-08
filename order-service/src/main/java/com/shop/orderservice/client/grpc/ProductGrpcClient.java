package com.shop.orderservice.client.grpc;

import com.shop.grpc.product.ProductGrpcRequest;
import com.shop.grpc.product.ProductGrpcResponse;
import com.shop.grpc.product.ProductGrpcServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Getter
@RequiredArgsConstructor
public class ProductGrpcClient extends ProductGrpcServiceGrpc.ProductGrpcServiceImplBase {

    private final ProductGrpcServiceGrpc.ProductGrpcServiceBlockingStub productBlockingStub;

    public ProductGrpcResponse getProductById(ProductGrpcRequest request) {
        try {
            return productBlockingStub
                    .withDeadlineAfter(60, TimeUnit.SECONDS) // set thời gian chết
                    .getProductById(request);
        } catch (StatusRuntimeException ex) {
            // Demo: ném lại để tầng gọi xử lý.
            // Production: chuyển status thành exception phù hợp.
            throw ex;
        }
    }
}
