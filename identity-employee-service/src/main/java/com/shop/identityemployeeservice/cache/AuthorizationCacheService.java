package com.shop.identityemployeeservice.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationCacheService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String PREFIX = "authz:user";
    private static final Duration TTL =
            Duration.ofMinutes(5);

    private String key(UUID userUuid, Long roleVersion) {
        return String.format(
                "%s:%s:v%d",
                PREFIX,
                userUuid,
                roleVersion
        );
    }
    public void save(UserAuthorizationCache cache){
        String redisKey = key(cache.userUuid(),cache.version());

        redisTemplate.delete(redisKey);

        if (!cache.permissions().isEmpty()) {
            redisTemplate.opsForSet().add(
                    redisKey,
                    cache.permissions().toArray(new String[0])
            );

            redisTemplate.expire(redisKey, TTL);
        }
    }

    public Optional<UserAuthorizationCache> find(UUID userUuid, Long version) {
        String redisKey = key(userUuid, version);

        Set<String> permissions = redisTemplate.opsForSet().members(redisKey);

        if (permissions == null || permissions.isEmpty()) {
            return Optional.empty();
        }

        // Đóng gói lại thành UserAuthorizationCache
        UserAuthorizationCache cache = new UserAuthorizationCache(userUuid, version, permissions);
        return Optional.of(cache);
    }



    public void delete(UUID userUuid, Long version) {
        redisTemplate.delete(key(userUuid, version));
    }

}
