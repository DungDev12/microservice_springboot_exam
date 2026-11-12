package com.shop.customerservice.service.interfaces;

import com.shop.customerservice.dto.request.AddressRequest;
import com.shop.customerservice.dto.response.AddressResponse;
import com.shop.customerservice.entity.Address;

public interface IAddressService {

    AddressResponse createAddress(String uuid, AddressRequest addressRequest);
    AddressResponse updateAddress(String uuid, long addressId, AddressRequest addressRequest);
    void deleteAddress(String uuid, long id);

    Address findAddressById(String uuid, long id);

    AddressResponse findAddressAndSetDefault(String uuid, long id);
}
