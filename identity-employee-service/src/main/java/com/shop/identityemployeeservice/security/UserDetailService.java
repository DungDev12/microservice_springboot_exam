package com.shop.identityemployeeservice.security;

import com.shop.identityemployeeservice.entity.EmployeeAccount;
import com.shop.identityemployeeservice.repository.EmployeeAccountRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final EmployeeAccountRepository employeeAccountRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        EmployeeAccount account = employeeAccountRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Username not found: " + username
                        )
                );

        // convert User => UserPrinciple
        return new UserPrincipal(
                account.getUuid(),
                account
        );
    }
}