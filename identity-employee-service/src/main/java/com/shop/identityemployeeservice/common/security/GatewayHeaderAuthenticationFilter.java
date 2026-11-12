package com.shop.identityemployeeservice.common.security;

import com.shop.identityemployeeservice.cache.AuthorizationCacheService;
import com.shop.identityemployeeservice.cache.AuthorizationQueryService;
import com.shop.identityemployeeservice.service.RoleService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GatewayHeaderAuthenticationFilter extends OncePerRequestFilter {

    private final RoleService roleService;
    private final AuthorizationQueryService authorizationQueryService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        UUID uuid;
        if(request.getHeader("X-Auth-UserUuid") != null){
            uuid = UUID.fromString(request.getHeader("X-Auth-UserUuid"));
            authorizationQueryService.getAuthorization(uuid);
        }
        String username = request.getHeader("X-Auth-User");
        String rolesHeader = request.getHeader("X-Auth-Roles");
        if (username != null && !username.isEmpty()) {
            List<GrantedAuthority> authorities = List.of();

            if (rolesHeader != null && !rolesHeader.isEmpty()) {
                authorities = Arrays.stream(rolesHeader.split(","))
                        .map(String::trim)
                        .filter(roleName -> !roleName.isEmpty())
                        .flatMap(roleName -> roleService.getPermissionByName(roleName).stream())
                        .distinct()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }

            // Tạo Authentication object (không cần password vì Gateway đã xác thực rồi)
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities
            );

            // Đưa vào SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
