package com.shop.identityemployeeservice.config;

import com.shop.identityemployeeservice.cache.AuthorizationCacheService;
import com.shop.identityemployeeservice.cache.AuthorizationQueryService;
import com.shop.identityemployeeservice.common.security.GatewayHeaderAuthenticationFilter;
import com.shop.identityemployeeservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final RoleService roleService;
    private final AuthorizationQueryService authorizationQueryService;

    // 1. Tiêm RoleService thông qua constructor của SecurityConfig

    @Bean
    public PasswordEncoder passwordEncoder(){
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
                        .requestMatchers("/api/accounts", "/api/accounts/**").permitAll()
                        .requestMatchers("/api/roles","/api/roles/**").permitAll()
                        .requestMatchers("/api/permissions","/api/permissions/**").permitAll()
                        .requestMatchers("/internal/authorization","/internal/authorization/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new GatewayHeaderAuthenticationFilter(roleService,authorizationQueryService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManager  ->
                        sessionManager.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


}
