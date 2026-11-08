package com.shop.productservice.service;

import com.shop.grpc.inventory.CreateInventoryRequest;
import com.shop.grpc.inventory.InventoryGrpcResponse;
import com.shop.grpc.product.OrderItems;
import com.shop.grpc.product.ProductGrpc;
import com.shop.grpc.product.ProductGrpcRequest;
import com.shop.grpc.product.ProductGrpcResponse;
import com.shop.productservice.client.grpc.InventoryGrpcClient;
import com.shop.productservice.dto.request.product.CreateProductRequest;
import com.shop.productservice.dto.request.product.UpdateProductRequest;
import com.shop.productservice.dto.response.product.ProductEmployeeResponse;
import com.shop.productservice.dto.response.product.ProductResponse;
import com.shop.productservice.entity.Product;
import com.shop.productservice.exception.NotFoundException;
import com.shop.productservice.mapper.ProductMapper;
import com.shop.productservice.repository.ProductRepository;
import com.shop.productservice.service.interfaces.IProductService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final InventoryGrpcClient  inventoryGrpcClient;

    @Override
    @Transactional
    public ProductEmployeeResponse createProduct(String employeeUuid ,CreateProductRequest request) {
        Product product = productMapper.toEntity(request);
        product.setCreateBy(UUID.fromString(employeeUuid));
        Product savedProduct = productRepository.save(product);
        InventoryGrpcResponse response = inventoryGrpcClient.createInventory(CreateInventoryRequest.newBuilder()
                        .setProductId(savedProduct.getId())
                        .setQuantity(request.quantity())
                .build());

         if(!response.getSuccess()){
             throw new InternalServerErrorException("Tạo sản phẩm thất bại");
         }
        return new ProductEmployeeResponse(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.isPromotion(),
                savedProduct.getPrice(),
                savedProduct.getDiscount(),
                savedProduct.getCreateBy().toString(),
                request.quantity(),
                savedProduct.getVat()
        );
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
    @Transactional
    public ProductEmployeeResponse updateProduct(Long id, String employeeUuid, UpdateProductRequest request) {
        Product product = this.findProductById(id);
        productMapper.updateProduct(request, product);
        return productMapper.toEmployeeResponse(productRepository.save(product));
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = this.findProductById(id);
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public ProductGrpcResponse getProductsForOrder(ProductGrpcRequest request) {

        List<Long> productIds = request.getOrderItemsList()
                .stream()
                .map(OrderItems::getProductId)
                .distinct()
                .toList();

        List<Product> products = productRepository.findAllById(productIds);

        return ProductGrpcResponse.newBuilder()
                .addAllProducts(
                        products.stream()
                                .map(product -> ProductGrpc.newBuilder()
                                        .setId(product.getId())
                                        .setName(product.getName())
                                        .setPrice(product.getPrice().toString())
                                        .setDiscount(product.getDiscount())
                                        .setVat(product.getVat())
                                        .build())
                                .toList()
                ).build();
    }
}
