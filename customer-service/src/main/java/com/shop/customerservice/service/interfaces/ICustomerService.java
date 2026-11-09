package com.shop.customerservice.service.interfaces;

import com.shop.customerservice.dto.request.UpdateCustomerRequest;
import com.shop.customerservice.dto.response.CustomerResponse;
import com.shop.customerservice.entity.Customer;
import com.shop.grpc.shared.CreateProfileRequest;

public interface ICustomerService {

    void createProfile(CreateProfileRequest request);
    CustomerResponse updateProfile(String uuid, UpdateCustomerRequest request);

    CustomerResponse getByCustomerId(String uuid);


    Customer findByUuid(String uuid);
}
