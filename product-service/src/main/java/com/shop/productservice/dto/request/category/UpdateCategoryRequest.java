package com.shop.productservice.dto.request.category;

public record UpdateCategoryRequest(
        String name,
        String description
) {
}
