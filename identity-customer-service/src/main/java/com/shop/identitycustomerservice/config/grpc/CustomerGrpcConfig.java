package com.shop.identitycustomerservice.config.grpc;

import com.shop.grpc.shared.CustomerGrpcServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;

@ImportGrpcClients(target = "customer-grpc-service", types = CustomerGrpcServiceGrpc.CustomerGrpcServiceBlockingStub.class)
@Configuration
public class CustomerGrpcConfig {

    @Bean
    public CustomerGrpcServiceGrpc.CustomerGrpcServiceBlockingStub customerGrpcServiceBlockingStub(DiscoveryClient discoveryClient) {
        // Tự động tìm danh sách instance của CUSTOMER-SERVICE trên Eureka Server
        var instances = discoveryClient.getInstances("customer-service");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Không tìm thấy instance nào của customer-service trên Eureka!");
        }

        // Lấy thông tin mạng của instance đầu tiên (Hoặc bạn có thể tùy biến giải thuật round-robin tại đây)
        var instance = instances.get(0);
        String host = instance.getHost();

        // LẤY PORT gRPC: Đọc từ metadata hoặc fall-back về port mặc định của gRPC Server bên kia (Ví dụ: 9090)
        int port = Integer.parseInt(instance.getMetadata().getOrDefault("gRPC.port", "9099"));

        // Khởi tạo kênh ManagedChannel thuần của gRPC
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        return CustomerGrpcServiceGrpc.newBlockingStub(channel);
    }
}
