package com.shop.productservice.client.rest;

import com.shop.productservice.cache.UserAuthorizationCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class IdentityAuthorizationClient {

    private final RestClient restClient;

    public IdentityAuthorizationClient(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder loadBalancedRestClientBuilder
    ) {
        this.restClient = loadBalancedRestClientBuilder
                .baseUrl("http://IDENTITY-EMPLOYEE-SERVICE")
                .build();
    }

    public UserAuthorizationCache getAuthorization(UUID userUuid) {
        UserAuthorizationCache result = restClient.get()
                .uri("/internal/authorization/{userUuid}", userUuid)
                .retrieve()
                .body(UserAuthorizationCache.class);

        if (result == null) {
            throw new IllegalStateException(
                    "Identity Service trả về dữ liệu rỗng"
            );
        }

        return result;
    }
}
