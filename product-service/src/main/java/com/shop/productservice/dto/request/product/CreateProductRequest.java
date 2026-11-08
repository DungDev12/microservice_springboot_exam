package com.shop.productservice.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


public record CreateProductRequest(
        String name,
        String description,
        Boolean promotion,
        BigDecimal price,
        Integer discount,
        Integer quantity,
        Integer vat
) {

}
