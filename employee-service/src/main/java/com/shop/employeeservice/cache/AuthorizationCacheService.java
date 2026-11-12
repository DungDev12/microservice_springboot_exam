package com.shop.employeeservice.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationCacheService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "authz:user";

    private String versionKey(UUID userUuid) {
        return PREFIX + ":" + userUuid + ":version";
    }

    private String permissionKey(UUID userUuid, Long version) {
        return PREFIX + ":" + userUuid + ":v" + version;
    }

    public Optional<UserAuthorizationCache> find(UUID userUuid) {

        // 1. Lấy version hiện tại
        String versionValue = redisTemplate.opsForValue()
                .get(versionKey(userUuid));

        if (versionValue == null) {
            return Optional.empty();
        }

        Long version = Long.parseLong(versionValue);

        // 2. Lấy permissions theo version
        Set<String> permissions = redisTemplate.opsForSet()
                .members(permissionKey(userUuid, version));

        if (permissions == null || permissions.isEmpty()) {
            return Optional.empty();
        }

        // 3. Trả kết quả
        return Optional.of(
                new UserAuthorizationCache(
                        userUuid,
                        version,
                        permissions
                )
        );
    }
}