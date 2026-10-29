package com.shop.productservice.mapper;

import com.shop.productservice.dto.request.product.CreateProductRequest;
import com.shop.productservice.dto.request.product.UpdateProductRequest;
import com.shop.productservice.dto.response.product.ProductEmployeeResponse;
import com.shop.productservice.dto.response.product.ProductResponse;
import com.shop.productservice.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(CreateProductRequest request);

    ProductEmployeeResponse toEmployeeResponse(Product entity);
    ProductResponse toResponse(Product entity);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateProduct(
            UpdateProductRequest request,
            @MappingTarget Product product
    );
}
