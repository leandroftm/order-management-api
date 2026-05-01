package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class UserAlreadyActiveException extends DomainException {
    public UserAlreadyActiveException() {
        super(
                "User already active",
                ErrorCode.USER_ALREADY_ACTIVE
        );
    }
}
