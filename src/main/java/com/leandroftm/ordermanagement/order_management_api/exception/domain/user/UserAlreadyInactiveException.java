package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class UserAlreadyInactiveException extends DomainException {
    public UserAlreadyInactiveException() {
        super(
                "User already inactive",
                ErrorCode.USER_ALREADY_INACTIVE
        );
    }
}
