package com.shop.employeeservice.cache;

import com.shop.employeeservice.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationQueryService {

    private final AuthorizationCacheService authorizationCacheService;

    public UserAuthorizationCache getAuthorization(
            UUID userUuid
    ) {

        Optional<UserAuthorizationCache> result =
                authorizationCacheService.find(userUuid);

        if (!result.isPresent()) {
            throw new AccessDeniedException("Quyền hết hạn", HttpStatus.FORBIDDEN);
        }
        return result.get();
    }

}
