package com.shop.employeeapigateway.filter;

import com.shop.employeeapigateway.security.JwtUtils;
import com.shop.employeeapigateway.security.RouterValidator;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtAuthenticationGatewayFilterFactory  extends AbstractGatewayFilterFactory<
        JwtAuthenticationGatewayFilterFactory.Config> {

    private final RouterValidator routerValidator;
    private final JwtUtils jwtUtils;

    public static class Config {
        // Có thể để trống nếu không có tham số cấu hình
    }

    // HTTP 401 khi loiS
    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public JwtAuthenticationGatewayFilterFactory(
            RouterValidator routerValidator,
            JwtUtils jwtUtils
    ) {
        super(Config.class);
        this.routerValidator = routerValidator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();

            if (routerValidator.predicate.test(request)) {
                // Kiểm tra có Header Authorization
                if(!request.getHeaders().containsHeader(HttpHeaders.AUTHORIZATION)){
                    System.out.println("Thiếu Header");
                    return this.onError(exchange,HttpStatus.UNAUTHORIZED);
                }

                String authToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                // Kiểm tra Bearer có trong Jwt không
                if(authToken == null || !authToken.startsWith("Bearer ")){
                    return this.onError(exchange,HttpStatus.UNAUTHORIZED);
                }

                String token = authToken.substring(7);
                if(!jwtUtils.validateToken(token)){
                    System.out.println("Token không hợp lệ");
                    return this.onError(exchange,HttpStatus.UNAUTHORIZED);
                }

                Claims claims = jwtUtils.getClaimsFromToken(token);
                log.info(String.format("[FILTER]-[%s]",claims.getId()));
                List<?> roles = claims.get("roles", List.class);

                String rolesHeader = roles == null
                        ? ""
                        : roles.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));

                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-Auth-UserUuid",claims.getId())
                        .header("X-Auth-User",claims.getSubject())
                        .header("X-Auth-Roles",rolesHeader)
                        .build();
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }
            return chain.filter(exchange);
        };
    }
}
