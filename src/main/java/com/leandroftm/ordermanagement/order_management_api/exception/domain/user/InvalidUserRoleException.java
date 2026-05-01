package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidUserRoleException extends DomainException {
    public InvalidUserRoleException() {
        super(
                "Invalid user role",
                ErrorCode.INVALID_USER_ROLE
        );
    }
}
