package com.shop.productservice.dto.response.product;

import java.math.BigDecimal;

public record ProductEmployeeResponse(
        Long id,
        String name,
        String description,
        boolean promotion,
        BigDecimal price,
        int discount,
        String createBy
) {
}
