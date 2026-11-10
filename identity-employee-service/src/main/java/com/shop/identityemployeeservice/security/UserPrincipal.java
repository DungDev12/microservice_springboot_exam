package com.shop.identityemployeeservice.security;

import com.shop.identityemployeeservice.entity.EmployeeAccount;
import com.shop.identityemployeeservice.entity.Permission;
import com.shop.identityemployeeservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private UUID id;
    private EmployeeAccount account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : account.getRoles()) {

            // ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE
            authorities.add(
                    new SimpleGrantedAuthority("ROLE_" + role.getName())
            );

            // CREATE_EMPLOYEE, UPDATE_EMPLOYEE, DELETE_EMPLOYEE...
            for (Permission permission : role.getPermissions()) {
                authorities.add(
                        new SimpleGrantedAuthority(permission.getName())
                );
            }
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public @Nullable String getPassword() {
        return account.getPasswordHash();
    }

}
