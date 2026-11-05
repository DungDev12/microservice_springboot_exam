package com.shop.orderservice.entity;

import com.shop.orderservice.common.entity.BaseLongEntity;
import com.shop.orderservice.common.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseLongEntity {

    private UUID customerUuid; // Mã khách hàng
    private String customerName; // Tên khách hàng mua hàng
    private String recipientName; // Tên khách nhận hàng
    private String recipientPhoneNumber; // Số điện thoại khách nhận
    private String recipientAddress; // Địa chỉ nhận hàng

    private String code; // Mã đơn hàng ( tạo ngẫu nhiên )

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems; // Các sản phâ trong đơn hàng

    private BigDecimal totalPrice; // Tổng giá trị đơn hàng
    private OrderStatus status; // Trạng thái đơn hàng ( WAIT_PAYMENT | PAYMENT | RETURN ....)

}
