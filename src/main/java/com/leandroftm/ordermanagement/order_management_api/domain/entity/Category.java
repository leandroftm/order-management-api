package com.leandroftm.ordermanagement.order_management_api.domain.entity;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryAlreadyActiveException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.InvalidCategoryNameException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.product.CategoryAlreadyInactiveException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        this.active = true;
    }

    public void update(String name) {
        if (name == null || name.isBlank())
            throw new InvalidCategoryNameException();
            this.name = name;
    }

    public void disable() {
        if (!this.active) {
            throw new CategoryAlreadyInactiveException();
        }
        deactivateInternal();
    }

    public void enable() {
        if (this.active) {
            throw new CategoryAlreadyActiveException();
        }
        activateInternal();
    }

    private void activateInternal() {
        this.active = true;
    }

    private void deactivateInternal() {
        this.active = false;
    }
}
