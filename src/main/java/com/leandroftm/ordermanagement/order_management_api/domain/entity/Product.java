package com.leandroftm.ordermanagement.order_management_api.domain.entity;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.ProductCategoryMismatchException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.InvalidStockException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.ProductAlreadyActiveException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.ProductAlreadyInactiveException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private boolean active;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product(String name,
                   String description,
                   BigDecimal price,
                   Integer stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public static Product create(
            String name,
            String description,
            BigDecimal price,
            Integer stock,
            Category category) {

        if (category == null) {
            throw new ProductCategoryMismatchException();
        }

        Product product = new Product(name, description, price, stock);
        product.setCategory(category);
        return product;
    }


    @PrePersist
    public void prePersist() {
        activateInternal();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void increaseStock(int amount) {
        this.stock += amount;
    }

    public void decreaseStock(int amount) {
        if (this.stock < amount) {
            throw new InvalidStockException();
        }
        this.stock -= amount;
    }

    public void updateDetails(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void disable() {
        if (!this.active) {
            throw new ProductAlreadyInactiveException();
        }
        deactivateInternal();
    }

    public void enable() {
        if (this.active) {
            throw new ProductAlreadyActiveException();
        }
        activateInternal();
    }

    private void activateInternal() {
        this.active = true;
    }

    private void deactivateInternal() {
        this.active = false;
    }

    public void updatePrice(BigDecimal newPrice) {
        this.price = newPrice;
    }
}
