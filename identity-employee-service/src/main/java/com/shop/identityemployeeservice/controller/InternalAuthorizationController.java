package com.shop.identityemployeeservice.controller;

import com.shop.identityemployeeservice.cache.UserAuthorizationCache;
import com.shop.identityemployeeservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/authorization")
@RequiredArgsConstructor
public class InternalAuthorizationController {

    private final AuthService  authService;

    @GetMapping("/{userUuid}")
    public ResponseEntity<UserAuthorizationCache> getAuthorization(
            @PathVariable String userUuid
    ) {

        UserAuthorizationCache result =
                authService.loadCurrentAuthorization(
                        userUuid
                );

        return ResponseEntity.ok(result);
    }
}
