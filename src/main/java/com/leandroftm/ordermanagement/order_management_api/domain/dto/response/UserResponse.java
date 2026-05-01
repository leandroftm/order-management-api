package com.leandroftm.ordermanagement.order_management_api.domain.dto.response;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.Order;
import com.leandroftm.ordermanagement.order_management_api.domain.entity.User;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(
        Long id,
        String email,
        String password,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Role role,
        List<Order> orders
) {
    public UserResponse(User user){
       this(
               user.getId(),
               user.getEmail(),
               user.getPassword(),
               user.isEnabled(),
               user.getCreatedAt(),
               user.getUpdatedAt(),
               user.getRole(),
               user.getOrders()
       );
    }
}
