package com.leandroftm.ordermanagement.order_management_api.domain.entity;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.NullProductException;
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
    private BigDecimal discountPercentage = BigDecimal.ZERO;
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

    //calculate item total price
    public BigDecimal calculateItemTotalPrice() {
        BigDecimal gross = calculateGrossTotal();
        return gross.subtract(gross.multiply(discountRate()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    //(price * quantity)
    private BigDecimal calculateGrossTotal() {
        if (unitPrice == null || quantity == null) {
            throw new IllegalStateException("Invalid order item state");
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    //return current discount
    private BigDecimal currentDiscount() {
        return (discountPercentage == null) ? BigDecimal.ZERO : discountPercentage;
    }

    //calculate discount
    private BigDecimal discountRate() {
        return currentDiscount().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
    }

    public void restoreStock() {
        if(this.product == null) {
            throw new NullProductException();
        }
        this.product.increaseStock(this.quantity);
    }
}
