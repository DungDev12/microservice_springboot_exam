package com.shop.orderservice.entity;

import com.shop.orderservice.common.entity.BaseLongEntity;
import com.shop.orderservice.common.enums.OrderPaymentStatus;
import com.shop.orderservice.common.enums.OrderStatus;
import com.shop.orderservice.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "order",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>(); // Các sản phẩm trong đơn hàng

    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.valueOf(0); // Tổng giá trị đơn hàng

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING_PAYMENT; // Trạng thái đơn hàng ( WAIT_PAYMENT | PAYMENT | RETURN ....)

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID; // Trạng thái thanh toán

    @Enumerated(EnumType.STRING)
    private OrderPaymentStatus paymentMethod; // Chuyển khoản hoặc thanh toán khi nhận hàng


}
