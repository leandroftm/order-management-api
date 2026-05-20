package com.leandroftm.ordermanagement.order_management_api.exception.domain.order;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DataIntegrityViolationException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidOrderException extends DataIntegrityViolationException {
    public InvalidOrderException() {
        super(
                "Invalid order",
                ErrorCode.INVALID_ORDER
        );
    }
}
