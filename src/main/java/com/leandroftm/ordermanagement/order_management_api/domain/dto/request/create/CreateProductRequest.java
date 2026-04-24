package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank
        @Size(min = 1, max = 100)
        String name,
        @Size(min = 0, max = 255)
        String description,
        @NotNull
        BigDecimal price,
        @NotNull
        Integer stock
) {
}
