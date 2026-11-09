package com.shop.customerservice.controller;

import com.shop.customerservice.dto.request.CustomerAddressRequest;
import com.shop.customerservice.dto.request.UpdateCustomerRequest;
import com.shop.customerservice.dto.response.ApiResponse;
import com.shop.customerservice.dto.response.CustomerAddressResponse;
import com.shop.customerservice.dto.response.CustomerResponse;
import com.shop.customerservice.service.AddressService;
import com.shop.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final AddressService addressService;

   @GetMapping("/hello")
    public String hello(
            @RequestHeader("X-Auth-Userid") String uuid
   ){
       return "Hello World " + uuid;
   }

   @GetMapping("/me")
   public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(
           @RequestHeader("X-Auth-Userid") String uuid
   ){
       return ResponseEntity.status(HttpStatus.OK)
               .body(ApiResponse.success(customerService.getByCustomerId(uuid),"Lấy thông tin thành công", HttpStatus.OK));
   }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomerInfo(
            @RequestHeader("X-Auth-Userid") String uuid,
            @RequestBody UpdateCustomerRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(customerService.updateProfile(uuid, request),
                        "Cập nhật thông tin thành công",
                        HttpStatus.OK));
    }

   @GetMapping("/me/addresses")
   public ResponseEntity<ApiResponse<CustomerAddressResponse>> getCustomerAddress(
           @RequestHeader("X-Auth-Userid") String uuid
   ) {
       return ResponseEntity.status(HttpStatus.OK)
               .body(ApiResponse.success(addressService.getAllAddressByCustomerUuid(uuid),
                       "Lấy địa chỉ thành công",HttpStatus.OK));
   }

   @PostMapping("/me/addresses")
   public ResponseEntity<ApiResponse<CustomerAddressResponse>> createCustomerAddress(
           @RequestHeader("X-Auth-Userid") String uuid,
           @RequestBody CustomerAddressRequest request
           ) {
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(ApiResponse.success(addressService.createAddress(uuid, request),
                       "Thêm địa chỉ thành công", HttpStatus.OK));
   }

    @PatchMapping("/me/addresses/{id}")
    public ResponseEntity<ApiResponse<CustomerAddressResponse>> updateCustomerAddress(
            @RequestHeader("X-Auth-Userid") String uuid,
            @PathVariable Long id,
            @RequestBody CustomerAddressRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(addressService.updateAddressById(uuid, id, request),
                        "Cập nhật thành công", HttpStatus.OK));
    }

    @PatchMapping("/me/address/{id}/default")
    public ResponseEntity<ApiResponse<CustomerAddressResponse>> updateCustomerDefaultAddress(
            @RequestHeader("X-Auth-Userid") String uuid,
            @PathVariable Long id
    ){
       return ResponseEntity.status(HttpStatus.OK)
               .body(ApiResponse.success(addressService.updateAddressDefault(uuid, id),
                       "Cập nhật thành công", HttpStatus.OK));
    }

    @DeleteMapping("/me/addresses/{id}")
    public ResponseEntity<ApiResponse<CustomerAddressResponse>> deleteCustomerAddress(
            @RequestHeader("X-Auth-Userid") String uuid,
            @PathVariable Long id
    ) {
       addressService.deleteAddressCustomerById(uuid,id);
       return ResponseEntity.status(HttpStatus.NO_CONTENT)
               .body(null);
    }
}
