package com.shop.customerservice.dto.request;

import com.shop.customerservice.entity.Address;

public record AddAddressRequest(
        Address address
) {
}
