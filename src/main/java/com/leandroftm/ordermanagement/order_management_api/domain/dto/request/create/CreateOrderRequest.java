package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @Valid
        @NotEmpty
        List<CreateOrderItemRequest> orderItems
) {
}
