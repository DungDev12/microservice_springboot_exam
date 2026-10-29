package com.shop.productservice.mapper;

import com.shop.productservice.dto.request.category.CreateCategoryRequest;
import com.shop.productservice.dto.request.category.UpdateCategoryRequest;
import com.shop.productservice.dto.response.category.CategoryEmployeeResponse;
import com.shop.productservice.dto.response.category.CategoryResponse;
import com.shop.productservice.entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "products", ignore = true)
    Category toEntity(CreateCategoryRequest request);
    CategoryResponse toResponse(Category category);
    CategoryEmployeeResponse toEmployeeResponse(Category category);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateCategory(
            UpdateCategoryRequest request,
            @MappingTarget Category category
    );
}
