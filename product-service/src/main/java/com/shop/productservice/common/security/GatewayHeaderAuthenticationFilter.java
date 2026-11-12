package com.shop.productservice.common.security;

import com.shop.productservice.cache.AuthorizationQueryService;
import com.shop.productservice.cache.UserAuthorizationCache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class GatewayHeaderAuthenticationFilter extends OncePerRequestFilter {

    private final AuthorizationQueryService authorizationQueryService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String userUuidHeader =
                request.getHeader("X-Auth-UserUuid");
        if(userUuidHeader != null) {
        UUID userUuid;
            userUuid = UUID.fromString(userUuidHeader);
            UserAuthorizationCache authorization =
                    authorizationQueryService.getAuthorization(userUuid);

            List<SimpleGrantedAuthority> authorities =
                    authorization.permissions()
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userUuid,
                            null,
                            authorities
                    );

            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }


        filterChain.doFilter(request, response);
    }
}
