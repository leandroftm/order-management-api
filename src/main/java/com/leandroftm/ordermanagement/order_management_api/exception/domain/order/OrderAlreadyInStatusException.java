package com.leandroftm.ordermanagement.order_management_api.exception.domain.order;

import com.leandroftm.ordermanagement.order_management_api.domain.enums.Status;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class OrderAlreadyInStatusException extends DomainException {
    public OrderAlreadyInStatusException(Status status) {
        super(
                "Order is already in status " + status,
                ErrorCode.ORDER_ALREADY_IN_STATUS
        );
    }
}
