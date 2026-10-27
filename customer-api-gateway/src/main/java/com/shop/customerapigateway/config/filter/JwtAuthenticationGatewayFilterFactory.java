package com.shop.customerapigateway.config.filter;

import com.shop.customerapigateway.security.JwtUtils;
import com.shop.customerapigateway.security.RouterValidator;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.ServerHttpRequest;

public class JwtAuthenticationGatewayFilterFactory  extends AbstractGatewayFilterFactory<
        JwtAuthenticationGatewayFilterFactory.Config> {

    private final RouterValidator routerValidator;
    private final JwtUtils jwtUtils;

    public static class Config {
        // Có thể để trống nếu không có tham số cấu hình
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

            // API công khai không cần JWT
            if (!routerValidator.predicate.test((ServerHttpRequest) request)) {
                return chain.filter(exchange);
            }

            return chain.filter(exchange);
        };
    }
}
