package com.shop.orderservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shop.grpc.product.ProductGrpc;
import com.shop.orderservice.common.enums.OrderPaymentStatus;
import com.shop.orderservice.common.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OrderResponse(
        String recipientName,
        String recipientPhoneNumber,
        String recipientAddress,
        String code,
        BigDecimal totalPrice,
        List<ProductResponseDTO> products,
        OrderStatus status,
        OrderPaymentStatus paymentMethod) {
    public record ProductResponseDTO(
            Long productId,
            String productName,
            BigDecimal price,
            Integer quantity,
            Integer discount,
            Integer vat,
            BigDecimal amount
    ){

    }
}
