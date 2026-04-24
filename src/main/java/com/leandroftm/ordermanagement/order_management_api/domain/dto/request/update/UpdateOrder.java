package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update;

import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;

import java.util.List;

public record UpdateOrder(
        Status status,
        List<UpdateOrderItemRequest> orderItems
        ) {
}
