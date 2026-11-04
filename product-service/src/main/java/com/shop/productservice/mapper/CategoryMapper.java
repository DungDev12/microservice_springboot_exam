package com.shop.productservice.mapper;

import com.shop.productservice.dto.request.category.CreateCategoryRequest;
import com.shop.productservice.dto.request.category.UpdateCategoryRequest;
import com.shop.productservice.dto.response.category.CategoryEmployeeResponse;
import com.shop.productservice.dto.response.category.CategoryResponse;
import com.shop.productservice.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CreateCategoryRequest request);

    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    CategoryResponse toResponse(Category category);
    CategoryEmployeeResponse toEmployeeResponse(Category category);


    void updateCategory(
            UpdateCategoryRequest request,
            @MappingTarget Category category
    );
}
