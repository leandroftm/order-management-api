package com.leandroftm.ordermanagement.order_management_api.security.dto;

public record LoginRequest(
        String email,
        String password
) {
}
