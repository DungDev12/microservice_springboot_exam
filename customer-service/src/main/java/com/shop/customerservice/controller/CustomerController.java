package com.shop.customerservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

   @GetMapping("/hello")
    public String hello(
            @RequestHeader("X-Auth-Userid") String uuid
   ){
       return "Hello World " + uuid;
   }
}
