package com.shop.productservice.dto.request.product;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        String description,
        Boolean  promotion ,
        BigDecimal price,
        Integer discount
) {
}
