package com.shop.identitycustomerservice.controller;

import com.shop.identitycustomerservice.dto.response.ApiResponse;
import com.shop.identitycustomerservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<?>> getAccount(@PathVariable String uuid) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(accountService.getEmailAccountByUuid(uuid),
                        "Lấy dữ liệu thành công",
                        HttpStatus.OK));
    }
}
