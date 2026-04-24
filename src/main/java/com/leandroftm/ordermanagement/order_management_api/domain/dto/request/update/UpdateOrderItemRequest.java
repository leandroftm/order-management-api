package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderItemRequest(
        @NotNull
        @Min(1)
        Integer quantity
) {
}
