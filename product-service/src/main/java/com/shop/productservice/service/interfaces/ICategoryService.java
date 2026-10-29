package com.shop.productservice.service.interfaces;

import com.shop.productservice.dto.request.category.CreateCategoryRequest;
import com.shop.productservice.dto.request.category.UpdateCategoryRequest;
import com.shop.productservice.dto.response.category.CategoryEmployeeResponse;
import com.shop.productservice.dto.response.category.CategoryResponse;
import com.shop.productservice.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    CategoryEmployeeResponse createCategory(String employeeUuid, CreateCategoryRequest request);
    CategoryEmployeeResponse updateCategory(Long categoryId, String employeeUuid, UpdateCategoryRequest request);

    Category findCategoryById(Long id);
    void deleteCategoryById(Long id);

    Page<CategoryResponse> findAllCategories(Pageable pageable);
}
