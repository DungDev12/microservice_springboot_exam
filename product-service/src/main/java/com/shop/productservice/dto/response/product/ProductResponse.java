package com.shop.productservice.dto.response.product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        boolean promotion,
        BigDecimal price,
        int discount
) {
}
