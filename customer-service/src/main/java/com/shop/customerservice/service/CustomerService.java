package com.shop.customerservice.service;

import com.shop.customerservice.dto.request.CreateCustomerRequest;
import com.shop.customerservice.dto.request.CustomerAddressRequest;
import com.shop.customerservice.dto.request.UpdateCustomerRequest;
import com.shop.customerservice.dto.response.CustomerAddressResponse;
import com.shop.customerservice.dto.response.CustomerResponse;
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
    public void createProfile(CreateProfileRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateBirth(LocalDate.parse(request.getDateBirth()))
                .build();

        customerRepository.save(customer);
    }

    @Override
    public CustomerResponse updateProfile(String uuid, UpdateCustomerRequest request) {
        Customer customer = this.findByUuid(uuid);
        customerMapper.update(request, customer);
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse getByCustomerId(String uuid) {
        return customerMapper.toResponse(this.findByUuid(uuid));
    }


    @Override
    public Customer findByUuid(String uuid) {
        return customerRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy khách hàng"));
    }


}
