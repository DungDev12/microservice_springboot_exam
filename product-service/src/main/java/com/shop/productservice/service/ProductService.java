package com.shop.productservice.service;

import com.shop.productservice.dto.request.product.CreateProductRequest;
import com.shop.productservice.dto.request.product.UpdateProductRequest;
import com.shop.productservice.dto.response.product.ProductEmployeeResponse;
import com.shop.productservice.dto.response.product.ProductResponse;
import com.shop.productservice.entity.Product;
import com.shop.productservice.exception.NotFoundException;
import com.shop.productservice.mapper.ProductMapper;
import com.shop.productservice.repository.ProductRepository;
import com.shop.productservice.service.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductEmployeeResponse createProduct(String employeeUuid ,CreateProductRequest request) {
        Product product = productMapper.toEntity(request);
        product.setCreateBy(UUID.fromString(employeeUuid));
        return productMapper.toEmployeeResponse(productRepository.save(product));
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));
    }

    @Override
    public Page<ProductResponse> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductEmployeeResponse updateProduct(Long id, String employeeUuid, UpdateProductRequest request) {
        Product product = this.findProductById(id);
        productMapper.updateProduct(request, product);
        return productMapper.toEmployeeResponse(productRepository.save(product));
    }

    @Override
    public void deleteProductById(Long id) {
        this.findProductById(id);
        productRepository.deleteById(id);
    }
}
