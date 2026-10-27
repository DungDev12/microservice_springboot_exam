package com.shop.customerservice.controller;

import com.shop.customerservice.dto.request.UpdateProfileRequest;
import com.shop.customerservice.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @PatchMapping("update/profile")
    public ResponseEntity<ApiResponse<?>> update(
            @Valid @RequestBody UpdateProfileRequest request
            ){
        return ResponseEntity.ok().build();
    }
}
