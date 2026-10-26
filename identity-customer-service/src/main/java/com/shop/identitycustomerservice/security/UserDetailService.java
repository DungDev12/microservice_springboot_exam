package com.shop.identitycustomerservice.security;

import com.shop.identitycustomerservice.entity.CustomerAccount;
import com.shop.identitycustomerservice.repository.CustomerAccountRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final CustomerAccountRepository customerAccountRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        CustomerAccount account = customerAccountRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Email not found: " + email
                        )
                );;

        // convert User => UserPrinciple
        return new UserPrincipal(
                account.getUuid(),
                account,
                List.of()
        );
    }
}