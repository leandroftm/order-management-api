package com.leandroftm.ordermanagement.order_management_api.domain.entity;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryNullException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.InsufficientStockException;
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
            throw new CategoryNullException();
        }

        Product product = new Product(name, description, price, stock);
        product.setCategory(category);
        return product;
    }


    @PrePersist
    public void prePersist() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    //move to service later
    public void increaseStock(int amount) {
        this.stock += amount;
    }

    //move to service later
    public void decreaseStock(int amount) {
        if (this.stock < amount) {
            throw new InsufficientStockException();
        }
        this.stock -= amount;
    }
}
