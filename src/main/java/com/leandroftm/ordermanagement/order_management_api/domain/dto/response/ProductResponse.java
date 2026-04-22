package com.leandroftm.ordermanagement.order_management_api.domain.dto.product;

import java.math.BigDecimal;

public record ProductResponse(
        String name,
        String description,
        BigDecimal price,
        Long categoryId
) {
}
