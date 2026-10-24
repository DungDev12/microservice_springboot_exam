package com.shop.customerservice.validation;

import com.shop.customerservice.repostitory.CustomerRepository;
import com.shop.grpc.shared.CreateProfileRequest;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerGrpcValidation {

    private final CustomerRepository customerRepository;

    public void validateRequest(CreateProfileRequest request){

    }

}
