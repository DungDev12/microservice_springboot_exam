package com.shop.identityemployeeservice.cache;

import com.shop.identityemployeeservice.entity.EmployeeAccount;
import com.shop.identityemployeeservice.exception.AccessDeniedException;
import com.shop.identityemployeeservice.exception.NotFoundException;
import com.shop.identityemployeeservice.repository.EmployeeAccountRepository;
import com.shop.identityemployeeservice.repository.PermissionRepository;
import com.shop.identityemployeeservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationQueryService {

    private final EmployeeAccountRepository employeeAccountRepository;
    private final AuthorizationCacheService authorizationCacheService;

    public UserAuthorizationCache getAuthorization(
            UUID userUuid
    ) {
        Long roleVersion = employeeAccountRepository.findRoleVersionByUserUuid(userUuid);

        Optional<UserAuthorizationCache> cached =
                authorizationCacheService.find(userUuid, roleVersion);

        if (cached.isPresent()) {
            return cached.get();
        }

        EmployeeAccount account = employeeAccountRepository
                .findById(userUuid)
                .orElseThrow(() -> new NotFoundException(
                        "Tài khoản không tồn tại"
                ));

        if (!account.isActive()) {
            throw new AccessDeniedException("Tài khoản đã bị khóa", HttpStatus.FORBIDDEN);
        }

        Set<String> permissions = employeeAccountRepository.findPermissionsByUserUuid(userUuid);

        UserAuthorizationCache result =
                new UserAuthorizationCache(
                        userUuid,
                        roleVersion,
                        permissions
                );

        authorizationCacheService.save(result);

        return result;
    }

}
