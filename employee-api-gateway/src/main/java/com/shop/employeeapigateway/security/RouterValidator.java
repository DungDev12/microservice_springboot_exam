package com.shop.employeeapigateway.security;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    public static final Set<String> PUBLIC_POST_ENDPOINTS  = Set.of(
            "/api/auth/login",
            "/api/auth/refreshToken"
    );

    private static final Set<String> PUBLIC_GET_ENDPOINTS = Set.of(
            "/api/roles",
            "/api/permissions",
            "/api/products",
            "/internal/authorization"
    );

    // kiem tra xem request di qua co nam trong danh sachs can bao ve hayko

    public Predicate<ServerHttpRequest> predicate =
            request -> {
                String path = request.getPath().value();
                HttpMethod method = request.getMethod();

                // Public POST endpoints
                if (method == HttpMethod.POST
                        && PUBLIC_POST_ENDPOINTS.contains(path)) {
                    return false;
                }

                // Public GET endpoints
                if (method == HttpMethod.GET
                        && PUBLIC_GET_ENDPOINTS.contains(path)) {
                    return false;
                }

                return true;
            };

}
