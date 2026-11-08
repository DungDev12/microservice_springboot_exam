package com.shop.orderservice.controller;


import com.shop.orderservice.config.sepay.SePayConfig;
import com.shop.orderservice.dto.request.SePayWebhookRequest;
import com.shop.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentWebhookController {

   private final SePayConfig sePayConfig;
   private final OrderService orderService;

    @PostMapping("/sepay-webhook")
    public ResponseEntity<String> handleSePayWebhook(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SePayWebhookRequest payload) {
        if (authHeader == null || authHeader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing Authorization Header");
        }

        String extractedToken = authHeader
                .replace("Bearer ", "")
                .replace("Apikey ", "");

        if (!extractedToken.equals(sePayConfig.getWebhookToken()) ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        // 2. Log dữ liệu test từ SePay gửi về Homelab
        System.out.println("===> NHẬN WEBHOOK TỪ SEPAY <===");
        System.out.println("Mã đơn hàng (Code): " + payload.code());

        orderService.paymentOrderByTransfer(payload);
        return ResponseEntity.ok("Success");
    }
}
