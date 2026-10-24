package com.shop.customerservice.entity;

import com.shop.customerservice.common.entity.BaseUUIDEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "customers")
public class Customer extends BaseUUIDEntity {

    private String firstName; // Họ
    private String lastName; // Tên
    private String phoneNumber; // Số điện thoại
    private LocalDate dateBirth;

    @OneToMany(mappedBy = "customer")
    private List<Address> addresses; // Danh sách địa chỉ giao hàng

}
