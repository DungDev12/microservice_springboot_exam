package com.shop.customerservice.entity;

import com.shop.customerservice.common.entity.BaseLongEntity;
import com.shop.customerservice.enums.TypeAddress;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "address")
public class Address extends BaseLongEntity {

    private String fullName; // Họ tên người nhận hàng
    private String district; // Huyện
    private String street; // Đường
    private String city; // Thành phố
    private String zip; // Mã thành phố
    private String country; // Quốc gia
    private String phoneNumber; // Số điện thoại liên hệ

    @Enumerated(EnumType.STRING)
    private TypeAddress type; // Nhà | Công ty

    @Column(name = "is_default")
    private boolean isDefault = false; // Địa chỉ mặc định

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "customer_uuid")
    private Customer customer;
}
