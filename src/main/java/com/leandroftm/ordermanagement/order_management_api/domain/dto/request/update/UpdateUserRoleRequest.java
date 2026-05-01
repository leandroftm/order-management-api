package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update;

import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
        @NotNull
        Role role
) {
}
