package com.shop.productservice.cache;

import com.shop.productservice.client.rest.IdentityAuthorizationClient;
import com.shop.productservice.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationQueryService {

    private final AuthorizationCacheService authorizationCacheService;
    private final IdentityAuthorizationClient  identityAuthorizationClient;

    public UserAuthorizationCache getAuthorization(
            UUID userUuid
    ) {

        return authorizationCacheService.find(userUuid)
                // Cache miss: gọi Identity Service
                .orElseGet(() ->
                        identityAuthorizationClient.getAuthorization(userUuid)
                );
    }

}
