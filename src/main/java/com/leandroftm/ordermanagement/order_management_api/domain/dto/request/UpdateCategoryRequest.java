package com.leandroftm.ordermanagement.order_management_api.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(
        @NotBlank
        String name
) {
}
