package com.leandroftm.ordermanagement.order_management_api.domain.dto.response;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.Order;
import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        Status status,
        Long userId,
        List<OrderItemResponse> orderItems
) {
    public OrderResponse(Order order) {
        this(
                order.getId(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getUser().getId(),
                order.getOrderItems().stream().map(OrderItemResponse::new).toList()
        );
    }
}
