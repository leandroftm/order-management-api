package com.leandroftm.ordermanagement.order_management_api.security.dto;

public record AuthResponse(
        String token,
        String type
) {
}
