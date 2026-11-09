package com.shop.customerservice.service;

import com.shop.customerservice.dto.request.CustomerAddressRequest;
import com.shop.customerservice.dto.response.CustomerAddressResponse;
import com.shop.customerservice.entity.Address;
import com.shop.customerservice.entity.Customer;
import com.shop.customerservice.exception.NotFoundException;
import com.shop.customerservice.mapper.AddressMapper;
import com.shop.customerservice.repostitory.AddressRepository;
import com.shop.customerservice.service.interfaces.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerService customerService;


    @Override
    public CustomerAddressResponse getAllAddressByCustomerUuid(String customerUuid) {
        return addressRepository.findAllByCustomerUuid(UUID.fromString(customerUuid));
    }

    @Override
    public CustomerAddressResponse createAddress(String uuid, CustomerAddressRequest request) {
        Customer customer = customerService.findByUuid(uuid);
        Address address = addressMapper.toEntity(request);
        address.setCustomer(customer);
        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    public CustomerAddressResponse updateAddressById(String uuid, Long addressId, CustomerAddressRequest request) {
        Address address = this.findByIdAndCustomerUuid(addressId, uuid);
        addressMapper.updateAddress(request, address);
        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    public CustomerAddressResponse updateAddressDefault(String uuid, Long id) {
        addressRepository.resetDefaultAddress(UUID.fromString(uuid));
        Address address = this.findByIdAndCustomerUuid(id, uuid);
        address.setDefault(true);
        return addressMapper.toResponse(addressRepository.save(address));
    }


    @Override
    public void deleteAddressCustomerById(String customerUuid, Long addressId) {
        addressRepository.deleteById(addressId);
    }

    @Override
    public Address findById(Long id) {
        return addressRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Không tìm thấy địa chỉ")
        );
    }

    @Override
    public Address findByIdAndCustomerUuid(Long id, String uuid) {
        return addressRepository.findByIdAndCustomer_Uuid(id, UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException("Địa chỉ không tìm thấy"));
    }

}
