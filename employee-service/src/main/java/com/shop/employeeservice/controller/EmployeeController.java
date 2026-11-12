package com.shop.employeeservice.controller;

import com.shop.employeeservice.dto.ApiResponse;
import com.shop.employeeservice.dto.request.UpdateInfoRequest;
import com.shop.employeeservice.dto.response.EmployeeResponse;
import com.shop.employeeservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<EmployeeResponse>> getMe(
            @RequestHeader("X-Auth-UserUuid") String userUuid
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        employeeService.getMe(userUuid),
                        "Lấy dữ liệu thành công",
                        HttpStatus.ACCEPTED
                ));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<EmployeeResponse>> postMe(
            @RequestHeader("X-Auth-UserUuid") String userUuid,
            @RequestBody UpdateInfoRequest updateInfoRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        employeeService.updateMeInfo(userUuid,updateInfoRequest),
                        "Cập nhật dữ liệu thành công",
                        HttpStatus.OK
                ));
    }
}
