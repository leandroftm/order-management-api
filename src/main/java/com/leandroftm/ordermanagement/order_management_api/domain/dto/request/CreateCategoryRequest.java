package com.leandroftm.ordermanagement.order_management_api.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank
        @Size(min = 1, max = 100)
        String name
) {
}
