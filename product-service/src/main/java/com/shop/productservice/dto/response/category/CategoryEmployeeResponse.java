package com.shop.productservice.dto.response.category;

public record CategoryEmployeeResponse(
        Long id,
        String name,
        String description,
        String createBy
) {
}
