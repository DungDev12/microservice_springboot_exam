package com.shop.customerservice.service.interfaces;

import com.shop.customerservice.dto.request.CreateCustomerRequest;
import com.shop.customerservice.dto.request.UpdateProfileRequest;
import com.shop.customerservice.dto.response.UpdateProfileResponse;
import com.shop.customerservice.entity.Customer;
import com.shop.grpc.shared.CreateProfileRequest;

public interface ICustomerService {

    Customer createProfile(CreateCustomerRequest request);
    UpdateProfileResponse updateProfile(String customerUuid, UpdateProfileRequest request);
    void createProfile(CreateProfileRequest request);
}
