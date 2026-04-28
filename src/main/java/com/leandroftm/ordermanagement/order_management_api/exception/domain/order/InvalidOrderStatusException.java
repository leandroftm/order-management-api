package com.leandroftm.ordermanagement.order_management_api.exception.domain.order;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidOrderStatusException extends DomainException {
    public InvalidOrderStatusException() {
        super(
                "Invalid order status",
                ErrorCode.INVALID_ORDER_STATUS
        );
    }
}
