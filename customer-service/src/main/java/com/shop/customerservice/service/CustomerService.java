package com.shop.customerservice.service;

import com.shop.customerservice.dto.request.UpdateCustomerRequest;
import com.shop.customerservice.dto.response.CustomerResponse;
import com.shop.customerservice.entity.Customer;
import com.shop.customerservice.exception.NotFoundException;
import com.shop.customerservice.mapper.CustomerMapper;
import com.shop.customerservice.repostitory.CustomerRepository;
import com.shop.customerservice.service.interfaces.ICustomerService;
import com.shop.grpc.shared.CreateProfileRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Transactional
    @Override
    public void createProfile(CreateProfileRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateBirth(LocalDate.parse(request.getDateBirth()))
                .build();
        customerRepository.save(customer);
    }

    @Transactional
    @Override
    public CustomerResponse updateProfile(String uuid, UpdateCustomerRequest request) {
        Customer customer = this.findById(uuid);
        customerMapper.update(request, customer);
        return customerMapper.toResponse(customerRepository.save(customer));
    }

    @Override
    public Customer findById(String uuid) {
        return customerRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException("Không tìm tấy khách hàng"));
    }
}
