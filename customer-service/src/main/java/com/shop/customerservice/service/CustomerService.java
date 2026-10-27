package com.shop.customerservice.service;

import com.shop.customerservice.common.enums.Gender;
import com.shop.customerservice.dto.request.CreateCustomerRequest;
import com.shop.customerservice.dto.request.UpdateProfileRequest;
import com.shop.customerservice.dto.response.UpdateProfileResponse;
import com.shop.customerservice.entity.Address;
import com.shop.customerservice.entity.Customer;
import com.shop.customerservice.exception.NotFoundException;
import com.shop.customerservice.mapper.CustomerMapper;
import com.shop.customerservice.repostitory.CustomerRepository;
import com.shop.customerservice.service.interfaces.ICustomerService;
import com.shop.grpc.shared.CreateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Customer createProfile(CreateCustomerRequest request) {
        return null;
    }

    @Override
    public UpdateProfileResponse updateProfile(String customerUuid, UpdateProfileRequest request) {
        Customer customer = customerRepository.findByUuid(UUID.fromString(customerUuid))
                .orElseThrow(() -> new NotFoundException("Khách hàng không tồn tại"));

        customerMapper.updateProfileFromDto(request, customer);
        return null;
    }

    @Override
    public void createProfile(CreateProfileRequest request) {

        Customer customer = Customer.builder()
                .uuid(UUID.fromString(request.getAccountUuid()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateBirth(LocalDate.parse(request.getDateBirth()))
                .phoneNumber(request.getPhoneNumber())
                .gender(Gender.valueOf(request.getGender()))
                .build();

        customerRepository.save(customer);
    }
}
