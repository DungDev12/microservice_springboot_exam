package com.shop.orderservice.entity;

import com.shop.orderservice.common.entity.BaseLongEntity;
import com.shop.orderservice.common.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@SuperBuilder
public class Order extends BaseLongEntity {

    private UUID customerUuid; // Mã khách hàng
    private String recipientName; // Tên khách nhận hàng
    private String recipientPhoneNumber; // Số điện thoại khách nhận
    private String recipientAddress; // Địa chỉ nhận hàng

    private String code; // Mã đơn hàng ( tạo ngẫu nhiên )

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems; // Các sản phẩm trong đơn hàng

    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.valueOf(0); // Tổng giá trị đơn hàng
    private OrderStatus status; // Trạng thái đơn hàng ( WAIT_PAYMENT | PAYMENT | RETURN ....)

}
