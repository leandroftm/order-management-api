package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DataIntegrityViolationException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class UserAlreadyExistsException extends DataIntegrityViolationException {
    public UserAlreadyExistsException() {
        super(
                "User already exists",
                ErrorCode.USER_ALREADY_EXISTS
        );
    }
}
