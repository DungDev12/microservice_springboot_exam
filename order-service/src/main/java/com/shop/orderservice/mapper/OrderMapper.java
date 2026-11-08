package com.shop.orderservice.mapper;

import com.shop.grpc.product.ProductGrpc;
import com.shop.grpc.product.ProductGrpcResponse;
import com.shop.orderservice.dto.request.CreateOrderRequest;
import com.shop.orderservice.dto.response.OrderResponse;
import com.shop.orderservice.entity.Order;
import com.shop.orderservice.entity.OrderItem;
import com.shop.orderservice.event.TransferNotificationEvent;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "orderItems", source = "items")
    Order toEntity(CreateOrderRequest request);

    @Mapping(target = "products", source = "orderItems")
    OrderResponse toResponse(Order order);

    @Named("convertPrice")
    default BigDecimal mapPrice(String price) {
        BigDecimal result = new BigDecimal(price);
        System.out.println(result.toPlainString());
        return result;
    }

    @Mapping(target = "name", source = "productName")
    TransferNotificationEvent.OrderItem toEvent(OrderItem orderItem);
    List<TransferNotificationEvent.OrderItem> toEventList(List<OrderItem> orderItems);
}
