package com.shop.identitycustomerservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserService {

    public UserPrincipal getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        assert authentication != null;
        return (UserPrincipal) authentication
                .getPrincipal();
    }

}
