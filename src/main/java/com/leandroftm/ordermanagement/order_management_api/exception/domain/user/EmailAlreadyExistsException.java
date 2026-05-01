package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException() {
        super(
                "Email already exists",
                ErrorCode.USER_EMAIL_ALREADY_EXISTS
        );
    }
}
