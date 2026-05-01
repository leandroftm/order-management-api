package com.leandroftm.ordermanagement.order_management_api.exception.domain.order;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidUserException extends DomainException {
    public InvalidUserException() {
        super(
                "Invalid user",
                ErrorCode.INVALID_USER
        );
    }
}
