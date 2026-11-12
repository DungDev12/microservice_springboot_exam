package com.shop.customerservice.controller;

import com.shop.customerservice.dto.request.UpdateCustomerRequest;
import com.shop.customerservice.dto.response.ApiResponse;
import com.shop.customerservice.entity.Customer;
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

   @GetMapping("/hello")
    public String hello(
            @RequestHeader("X-Auth-Userid") String uuid
   ){
       return "Hello World " + uuid;    
   }

   @PostMapping("update/info")
    public ResponseEntity<ApiResponse<?>> updateCustomerInfo(
            @RequestHeader("X-Auth-Userid") String uuid,
            @RequestBody UpdateCustomerRequest request
   ){
       return ResponseEntity.status(HttpStatus.OK)
               .body(ApiResponse.success(
                       customerService.updateProfile(uuid,request),
                       "Cập nhật thành công",
                       HttpStatus.OK
               ));
   }
}
