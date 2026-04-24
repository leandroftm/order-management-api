package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemRequest(
        @NotNull
        @Positive
        @Min(1)
        Integer quantity,
        @NotNull
        Long productId
) {
}
