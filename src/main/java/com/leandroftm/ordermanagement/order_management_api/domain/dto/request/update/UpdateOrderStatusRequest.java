package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update;

import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;

public record UpdateOrderStatusRequest(
        Status status
) {
}
