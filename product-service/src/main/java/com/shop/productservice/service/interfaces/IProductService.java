package com.shop.productservice.service.interfaces;

import com.shop.grpc.product.ProductGrpcRequest;
import com.shop.grpc.product.ProductGrpcResponse;
import com.shop.productservice.dto.request.product.CreateProductRequest;
import com.shop.productservice.dto.request.product.UpdateProductRequest;
import com.shop.productservice.dto.response.product.ProductEmployeeResponse;
import com.shop.productservice.dto.response.product.ProductResponse;
import com.shop.productservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    ProductEmployeeResponse createProduct(String employeeUuid, CreateProductRequest request);

    Product findProductById(Long id);
    Page<ProductResponse> findAllProducts(Pageable pageable);

    ProductEmployeeResponse updateProduct(Long id, String employeeUuid, UpdateProductRequest request);

    void deleteProductById(Long id);

    ProductGrpcResponse getProductsForOrder(ProductGrpcRequest request);
}
