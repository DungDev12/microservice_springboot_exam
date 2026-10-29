package com.shop.productservice.dto.request.product;

import java.math.BigDecimal;

public record CreateProductRequest(
        String name,
        String description,
        boolean promotion,
        BigDecimal price,
        int discount
) {
}
