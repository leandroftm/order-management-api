package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidCredentials extends DomainException {
    public InvalidCredentials() {
        super(
                "Invalid Credentials",
                ErrorCode.INVALID_CREDENTIALS
        );
    }
}
