package com.shop.productservice.cache;

import java.util.Set;
import java.util.UUID;

public record UserAuthorizationCache(
        UUID userUuid,
        Long version,
        Set<String> permissions
) {
}
