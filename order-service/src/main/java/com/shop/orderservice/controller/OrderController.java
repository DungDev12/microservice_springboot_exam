package com.shop.orderservice.controller;

import com.shop.orderservice.dto.request.AddOrderRequest;
import com.shop.orderservice.dto.request.CreateOrderRequest;
import com.shop.orderservice.dto.request.PaymentOrderRequest;
import com.shop.orderservice.dto.response.ApiResponse;
import com.shop.orderservice.dto.response.OrderResponse;
import com.shop.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestHeader("X-Auth-Userid") String uuid,
            @RequestBody CreateOrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(orderService.createOrder(uuid, request),
                        "Tạo giỏ hàng thành công",
                        HttpStatus.CREATED));
    }

    @PostMapping("{orderCode}")
    public ResponseEntity<ApiResponse<OrderResponse>> addOrderById(
            @RequestHeader("X-Auth-Userid") String uuid,
            @PathVariable String orderCode,
            @RequestBody AddOrderRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(orderService.updateOrderItemsByOrderId(uuid,orderCode,request),
                        "Thêm thành công", HttpStatus.OK));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders(
            @RequestHeader("X-Auth-Userid") String uuid,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "updatedAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.page(orderService.getAllOrders(uuid,pageable), "Lấy dữ liệu thành công"));
    }

    @PostMapping("pay")
    public ResponseEntity<ApiResponse<?>> payOrder(
            @RequestHeader("X-Auth-Userid") String uuid,
            @RequestBody PaymentOrderRequest request
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(orderService.paymentOrderByCode(uuid,request),
                        "Tạo hình thức thanh toán", HttpStatus.OK));
    }

    @GetMapping("{orderCode}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @RequestHeader("X-Auth-Userid") String uuid,
            @PathVariable String orderCode
    ){
        orderService.orderCancel(uuid,orderCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(null);
    }
}
