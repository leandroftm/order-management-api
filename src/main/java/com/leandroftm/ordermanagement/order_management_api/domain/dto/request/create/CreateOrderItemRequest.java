package com.leandroftm.ordermanagement.order_management_api.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequest(
        @NotNull
        @Min(1)
        Integer quantity,
        @NotNull
        Long productId
) {
}
