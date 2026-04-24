package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequest(
        @NotBlank
        String name
) {
}
