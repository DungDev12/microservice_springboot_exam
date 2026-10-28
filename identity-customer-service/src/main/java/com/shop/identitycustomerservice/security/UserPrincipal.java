package com.shop.identitycustomerservice.security;

import com.shop.identitycustomerservice.entity.CustomerAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private UUID id;
    private CustomerAccount account;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return account.getEmail();
    }

    @Override
    public @Nullable String getPassword() {
        return account.getPasswordHash();
    }

}
