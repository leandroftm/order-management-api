package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException() {
        super(
                "Email is invalid",
                ErrorCode.INVALID_EMAIL
        );
    }
}
