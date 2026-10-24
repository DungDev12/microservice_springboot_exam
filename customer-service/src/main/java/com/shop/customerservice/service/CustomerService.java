package com.shop.customerservice.service;

import com.shop.customerservice.dto.request.CreateCustomerRequest;
import com.shop.customerservice.entity.Address;
import com.shop.customerservice.entity.Customer;
import com.shop.customerservice.repostitory.CustomerRepository;
import com.shop.customerservice.service.interfaces.ICustomerService;
import com.shop.grpc.shared.CreateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer createProfile(CreateCustomerRequest request) {
        return null;
    }

    @Override
    public void createProfile(CreateProfileRequest request) {

        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateBirth(LocalDate.parse(request.getDateBirth()))
                .build();

        customerRepository.save(customer);
    }
}
