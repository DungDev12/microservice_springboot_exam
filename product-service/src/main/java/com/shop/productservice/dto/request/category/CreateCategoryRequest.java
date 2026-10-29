package com.shop.productservice.dto.request.category;

public record CreateCategoryRequest(
        String name,
        String description
) {
}
