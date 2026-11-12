package com.shop.customerservice.controller;

import com.shop.customerservice.dto.request.AddressRequest;
import com.shop.customerservice.dto.response.AddressResponse;
import com.shop.customerservice.dto.response.ApiResponse;
import com.shop.customerservice.entity.Address;
import com.shop.customerservice.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/addresses")
@RequiredArgsConstructor
public class CustomerAddressesController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @RequestHeader("X-Auth-Userid") String uuid,
            @RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        addressService.createAddress(uuid,request),
                        "Tạo địa chỉ thành công",
                        HttpStatus.CREATED
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @RequestHeader("X-Auth-Userid") String uuid,
            @RequestBody AddressRequest request,
            @PathVariable long id
    ){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        addressService.updateAddress(uuid,id,request),
                        "Tạo địa chỉ thành công",
                        HttpStatus.CREATED
                ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> deleteAddress(
            @RequestHeader("X-Auth-Userid") String uuid,
            @PathVariable long id
    ){
        addressService.deleteAddress(uuid,id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(null);
    }

    @PostMapping("/{id}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> addDefaultAddress(
            @RequestHeader("X-Auth-Userid") String uuid,
            @PathVariable long id
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(addressService.findAddressAndSetDefault(uuid,id),
                        "Cập nhật thành công",
                        HttpStatus.OK));
    }
}
