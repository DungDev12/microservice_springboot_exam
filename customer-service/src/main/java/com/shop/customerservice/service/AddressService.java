package com.shop.customerservice.service;

import com.shop.customerservice.dto.request.AddressRequest;
import com.shop.customerservice.dto.response.AddressResponse;
import com.shop.customerservice.entity.Address;
import com.shop.customerservice.exception.NotFoundException;
import com.shop.customerservice.mapper.AddressMapper;
import com.shop.customerservice.repostitory.AddressRepository;
import com.shop.customerservice.service.interfaces.IAddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService  implements IAddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponse createAddress(String uuid, AddressRequest addressRequest) {
        Address address =addressMapper.toEntity(addressRequest);
        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    public AddressResponse updateAddress(String uuid, long addressId, AddressRequest addressRequest) {
        Address address = this.findAddressById(uuid, addressId);
        addressMapper.update(addressRequest,address);
        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    public void deleteAddress(String uuid,long id) {
        addressRepository.deleteByIdAndCustomer_Uuid(id, UUID.fromString(uuid));
    }

    @Override
    public Address findAddressById(String uuid, long id) {
        return  addressRepository.findByIdAndCustomer_Uuid(id,UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy địa chỉ"));
    }

    @Transactional
    @Override
    public AddressResponse findAddressAndSetDefault(String uuid, long id) {
        Address address = this.findAddressById(uuid, id);
        addressRepository.clearDefaultAddress(UUID.fromString(uuid));
        address.setDefault(true);
        return addressMapper.toResponse(addressRepository.save(address));
    }


}
