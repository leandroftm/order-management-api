package com.leandroftm.ordermanagement.order_management_api.exception.domain.order;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.NotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException() {
        super(
                "Order not found",
                ErrorCode.ORDER_NOT_FOUND
        );
    }
}
