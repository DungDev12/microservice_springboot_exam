package com.shop.identitycustomerservice.validator;

import com.shop.identitycustomerservice.dto.request.RegisterAccountRequest;
import com.shop.identitycustomerservice.repository.CustomerAccountRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final CustomerAccountRepository customerAccountRepository;

    public void registerValidation(RegisterAccountRequest request){
        checkEmail(request.email());
    }

    protected void checkEmail(String email){
        if(customerAccountRepository.existsByEmailIgnoreCase(email)){
            throw new ValidationException("Email already exists");
        }
    }
}
