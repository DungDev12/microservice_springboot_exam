package com.shop.productservice.controller;

import com.shop.productservice.dto.request.product.CreateProductRequest;
import com.shop.productservice.dto.request.product.UpdateProductRequest;
import com.shop.productservice.dto.response.ApiResponse;
import com.shop.productservice.dto.response.product.ProductEmployeeResponse;
import com.shop.productservice.dto.response.product.ProductResponse;
import com.shop.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("create")
    public ResponseEntity<ApiResponse<ProductEmployeeResponse>> create(
            @RequestHeader("X-Auth-Userid") String uuid,
            @RequestBody CreateProductRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        productService.createProduct(uuid,request),
                        "Tạo sản phẩm thành công",
                        HttpStatus.CREATED
                ));
    }

    @PutMapping("{id}/update")
    public ResponseEntity<ApiResponse<ProductEmployeeResponse>> updateProduct(
            @PathVariable Long id,
            @RequestHeader("X-Auth-Userid") String uuid,
            @RequestBody UpdateProductRequest request
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        productService.updateProduct(id,uuid,request),
                        "Tạo sản phẩm thành công",
                        HttpStatus.OK
                ));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(
            @PathVariable Long id
    ){
        productService.deleteProductById(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT)
              .body(ApiResponse.success("Xoá dữ liệu  thành công"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> findAllProducts(
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "name",
                    direction = Sort.Direction.ASC
            ) Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.page(
                        productService.findAllProducts(pageable),
                        "Lấy danh sách sản phẩm thành công"
                ));
    }
}
