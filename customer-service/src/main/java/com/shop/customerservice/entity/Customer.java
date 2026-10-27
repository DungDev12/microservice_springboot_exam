package com.shop.customerservice.entity;

import com.shop.customerservice.common.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "customers")
public class Customer{

    @Id
    @Column(nullable = false, updatable = false)
    private UUID uuid;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    private String firstName; // Họ
    private String lastName; // Tên
    private String phoneNumber; // Số điện thoại
    private LocalDate dateBirth;
    private Gender gender;

    @OneToMany(mappedBy = "customer")
    private List<Address> addresses; // Danh sách địa chỉ giao hàng

}
