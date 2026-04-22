package com.leandroftm.ordermanagement.order_management_api.exception.domain;

import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class NotFoundException extends DomainException {
    public NotFoundException(String message, ErrorCode errorCode) {
        super(
                message, errorCode
        );
    }
}
