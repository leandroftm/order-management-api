package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update;

import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(max = 100)
        String email,
        @NotBlank
        @Size(min = 6, max = 20)
        String password
) {
}
