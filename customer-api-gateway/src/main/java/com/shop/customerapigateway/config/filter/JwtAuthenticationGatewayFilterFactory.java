package com.shop.customerapigateway.config.filter;

import com.shop.customerapigateway.security.JwtUtils;
import com.shop.customerapigateway.security.RouterValidator;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
    public GatewayFilter apply(JwtAuthenticationGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            System.out.println("Apply Test");
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
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-Auth-Userid",claims.getId())
                        .header("X-Auth-User",claims.getSubject())
                        .header("X-Auth-Roles",claims.get("roles",String.class))
                        .build();
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }
            return chain.filter(exchange);
        };
    }
}
