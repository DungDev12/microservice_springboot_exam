package com.shop.productservice.service;

import com.shop.productservice.dto.request.category.CreateCategoryRequest;
import com.shop.productservice.dto.request.category.UpdateCategoryRequest;
import com.shop.productservice.dto.response.category.CategoryEmployeeResponse;
import com.shop.productservice.dto.response.category.CategoryResponse;
import com.shop.productservice.entity.Category;
import com.shop.productservice.exception.NotFoundException;
import com.shop.productservice.mapper.CategoryMapper;
import com.shop.productservice.repository.CategoryRepository;
import com.shop.productservice.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryEmployeeResponse createCategory(String employeeUuid , CreateCategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        category.setCreateBy(UUID.fromString(employeeUuid));
        // Chưa xử lý lấy dữ tên nhân viên
        return categoryMapper.toEmployeeResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryEmployeeResponse updateCategory(Long categoryId, String employeeUuid, UpdateCategoryRequest request) {
        Category category = this.findCategoryById(categoryId);
        categoryMapper.updateCategory(request, category);
        // Chưa xử lý lấy dữ tên nhân viên
        return categoryMapper.toEmployeeResponse(
                categoryRepository.save(category)
        );
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Thể loại không tìm thấy"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        this.findCategoryById(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<CategoryResponse> findAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toResponse);
    }
}
