package com.leandroftm.ordermanagement.order_management_api.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private BigDecimal unitPrice = BigDecimal.ZERO;
    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private BigDecimal discount = BigDecimal.ZERO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    public void snapshotPrice() {
        if (product != null) {
            this.unitPrice = product.getPrice();
        }
    }

    public BigDecimal calcItemTotalPrice() {
        if (product == null || product.getPrice() == null || quantity == null) {
            return BigDecimal.ZERO;
        }

        //(price * quantity)
        BigDecimal grossTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        BigDecimal currentDiscount = (discount == null) ? BigDecimal.ZERO : discount;

        //calculate discount
        BigDecimal discountMultiplier = currentDiscount.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

        //calculate discount amount
        BigDecimal discountAmount = grossTotal.multiply(discountMultiplier);

        return grossTotal.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }
}
