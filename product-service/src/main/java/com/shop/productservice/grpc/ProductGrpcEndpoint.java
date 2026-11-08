package com.shop.productservice.grpc;

import com.shop.grpc.product.ProductGrpcRequest;
import com.shop.grpc.product.ProductGrpcResponse;
import com.shop.grpc.product.ProductGrpcServiceGrpc;
import com.shop.productservice.entity.Product;
import com.shop.productservice.service.ProductService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class ProductGrpcEndpoint extends ProductGrpcServiceGrpc.ProductGrpcServiceImplBase {

    private final ProductService productService;

    @Override
    public void getProductById(ProductGrpcRequest request,
                               StreamObserver<ProductGrpcResponse> responseObserver){
        try{
            ProductGrpcResponse response = productService.getProductsForOrder(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex){
            responseObserver.onError(Status.ALREADY_EXISTS
                    .withDescription("Không thể lấy dữ liệu")
                    .asRuntimeException());
        }
    }
}
