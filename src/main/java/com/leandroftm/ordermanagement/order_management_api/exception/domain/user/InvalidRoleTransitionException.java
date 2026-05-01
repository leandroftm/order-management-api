package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidRoleTransitionException extends DomainException {
    public InvalidRoleTransitionException() {
        super(
                "Invalid role transition",
                ErrorCode.INVALID_ROLE_TRANSITION
        );
    }
}
