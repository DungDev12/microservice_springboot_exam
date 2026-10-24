package com.shop.identitycustomerservice;

import com.shop.identitycustomerservice.client.grpc.CustomerGrpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.client.ImportGrpcClients;

@SpringBootApplication
@ImportGrpcClients(basePackageClasses =  CustomerGrpcClient.class)
public class IdentityCustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityCustomerServiceApplication.class, args);
    }

}
