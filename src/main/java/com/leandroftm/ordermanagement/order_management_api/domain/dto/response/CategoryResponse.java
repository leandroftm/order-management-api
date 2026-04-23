package com.leandroftm.ordermanagement.order_management_api.domain.dto.response;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.Category;

import java.util.List;

public record CategoryResponse(
        String name,
        List<ProductResponse> products
) {

    public CategoryResponse(Category category) {
        this(
                category.getName(),
                category.getProducts()
                        .stream().map(ProductResponse::new).toList()
        );
    }
}
