package com.shop.productservice.controller;

import com.shop.productservice.dto.request.category.CreateCategoryRequest;
import com.shop.productservice.dto.request.category.UpdateCategoryRequest;
import com.shop.productservice.dto.response.ApiResponse;
import com.shop.productservice.dto.response.category.CategoryEmployeeResponse;
import com.shop.productservice.dto.response.category.CategoryResponse;
import com.shop.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('CREATE_CATEGORY')")
    @PostMapping("create")
    public ResponseEntity<ApiResponse<CategoryEmployeeResponse>> create(
            @RequestHeader("X-Auth-UserUuid") String uuid,
            @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(categoryService.createCategory(uuid,request),
                                "Tạo thể loại thành công",
                                HttpStatus.CREATED)
                );
    }

    @PreAuthorize("hasAuthority('UPDATE_CATEGORY')")
    @PutMapping("{id}/update")
    public ResponseEntity<ApiResponse<CategoryEmployeeResponse>> update(
            @PathVariable Long id,
            @RequestHeader("X-Auth-UserUuid") String uuid,
            @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(categoryService.updateCategory(id,uuid,request),
                        "Cập nhật dữ liệu thành công",
                        HttpStatus.OK));
    }

    @PreAuthorize("hasAuthority('DELETE_CATEGORY')")
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(
                        ApiResponse.success("Xoá dữ liệu thành công")
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.page(categoryService.findAllCategories(pageable),
                                "Lấy danh sách thành công")
                );
    }
}
