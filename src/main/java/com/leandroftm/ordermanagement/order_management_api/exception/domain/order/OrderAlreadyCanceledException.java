package com.leandroftm.ordermanagement.order_management_api.exception.domain.order;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class OrderAlreadyCanceledException extends DomainException {
    public OrderAlreadyCanceledException() {
        super(
                "Order is already canceled",
                ErrorCode.ORDER_ALREADY_CANCELED
        );
    }
}
