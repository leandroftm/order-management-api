package com.leandroftm.ordermanagement.order_management_api.domain.dto.response;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        String name,
        String description,
        BigDecimal price,
        Long categoryId
) {
    public ProductResponse(Product product) {
        this(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getId()
        );
    }
}
