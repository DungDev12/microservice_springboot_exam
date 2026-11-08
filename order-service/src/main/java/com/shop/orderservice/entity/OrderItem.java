package com.shop.orderservice.entity;

import com.shop.orderservice.common.entity.BaseLongEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_items")
@SuperBuilder
public class OrderItem extends BaseLongEntity {

    private Long productId; // ID sản phẩm
    private String productName; // Tên sản phẩm
    private Long quantity; // số lượng đặt mua
    private BigDecimal price; // Giá tiền lúc mua
    private int discount; // Giảm giá ( nếu có )
    private int vat; // Thuế VAT lúc đó

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // 1. Hàm tính tổng tiền hàng gốc (Chưa trừ giảm giá, chưa cộng thuế)
    public BigDecimal getRawAmount() {
        return price.multiply(new BigDecimal(quantity));
    }

    // 2. Hàm tính riêng số tiền được GIẢM GIÁ (Tính trên tổng tiền hàng gốc)
    public BigDecimal getDiscountAmount() {
        if (discount <= 0) return BigDecimal.ZERO;

        BigDecimal rawAmount = getRawAmount();
        BigDecimal discountRate = new BigDecimal(discount).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);

        // Tiền giảm giá = Tiền gốc * (Phần trăm giảm giá / 100)
        return rawAmount.multiply(discountRate).setScale(0, RoundingMode.HALF_UP);
    }

    // 3. Hàm tính riêng số tiền THUẾ VAT (Tính trên số tiền sau - giảm giá)
    public BigDecimal getVatAmount() {
        if (vat <= 0) return BigDecimal.ZERO;

        // Tiền chịu thuế = Tiền gốc - Tiền giảm giá
        BigDecimal amountAfterDiscount = getRawAmount().subtract(getDiscountAmount());
        BigDecimal vatRate = new BigDecimal(vat).divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);

        // Tiền thuế VAT = Tiền sau giảm giá * (Phần trăm thuế / 100)
        return amountAfterDiscount.multiply(vatRate).setScale(0, RoundingMode.HALF_UP);
    }

    // 4. HÀM TỔNG CUỐI CÙNG: Bao gồm cả thuế và trừ giảm giá
    public BigDecimal getAmount() {
        BigDecimal rawAmount = getRawAmount();
        BigDecimal discountAmount = getDiscountAmount();
        BigDecimal vatAmount = getVatAmount();

        // Công thức: Tổng = Tiền gốc - Giảm giá + Thuế VAT
        return rawAmount.subtract(discountAmount).add(vatAmount);
    }
}
