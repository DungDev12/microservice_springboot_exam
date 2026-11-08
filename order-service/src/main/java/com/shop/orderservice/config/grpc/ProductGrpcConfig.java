package com.shop.orderservice.config.grpc;

import com.shop.grpc.product.ProductGrpcServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductGrpcConfig {

    @Bean
    public ProductGrpcServiceGrpc.ProductGrpcServiceBlockingStub productGrpcServiceBlockingStub(DiscoveryClient discoveryClient) {
        // Tự động tìm danh sách instance của CUSTOMER-SERVICE trên Eureka Server
        var instances = discoveryClient.getInstances("product-service");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Không tìm thấy instance nào của product-service trên Eureka!");
        }

        // Lấy thông tin mạng của instance đầu tiên (Hoặc bạn có thể tùy biến giải thuật round-robin tại đây)
        var instance = instances.get(0);
        String host = instance.getHost();

        // LẤY PORT gRPC: Đọc từ metadata hoặc fall-back về port mặc định của gRPC Server bên kia (Ví dụ: 9090)
        int port = Integer.parseInt(instance.getMetadata().getOrDefault("gRPC.port", "9098"));

        // Khởi tạo kênh ManagedChannel thuần của gRPC
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        return ProductGrpcServiceGrpc.newBlockingStub(channel);
    }

}
