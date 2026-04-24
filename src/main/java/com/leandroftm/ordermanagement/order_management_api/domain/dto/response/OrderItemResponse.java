package com.leandroftm.ordermanagement.order_management_api.domain.dto.response;

import com.leandroftm.ordermanagement.order_management_api.domain.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productName,
        String categoryName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal discount,
        BigDecimal total
) {
    public OrderItemResponse(OrderItem orderItem){
        this(
                orderItem.getProduct().getName(),
                orderItem.getProduct().getCategory().getName(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice(),
                orderItem.getDiscount(),
                orderItem.calculateItemTotalPrice()
        );
    }
}
