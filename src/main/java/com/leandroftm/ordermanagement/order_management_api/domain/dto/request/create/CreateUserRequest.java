package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.create;

import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import jakarta.validation.constraints.*;

public record CreateUserRequest(
        @NotBlank
        @Email
        @Max(100)
        String email,
        @NotBlank
        @Size(min = 6, max = 100)
        String password,
        @NotNull
        Role role
) {
}
