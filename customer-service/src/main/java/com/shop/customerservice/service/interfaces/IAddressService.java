package com.shop.customerservice.service.interfaces;

import com.shop.customerservice.dto.request.CustomerAddressRequest;
import com.shop.customerservice.dto.response.CustomerAddressResponse;
import com.shop.customerservice.entity.Address;

public interface IAddressService {


    CustomerAddressResponse getAllAddressByCustomerUuid(String uuid);
    CustomerAddressResponse createAddress(String uuid, CustomerAddressRequest request);
    CustomerAddressResponse updateAddressById(String uuid, Long addressId, CustomerAddressRequest request);
    CustomerAddressResponse updateAddressDefault(String uuid, Long id);

    void deleteAddressCustomerById(String uuid, Long addressId);

    Address findById(Long id);

    Address findByIdAndCustomerUuid(Long id, String uuid);
}
