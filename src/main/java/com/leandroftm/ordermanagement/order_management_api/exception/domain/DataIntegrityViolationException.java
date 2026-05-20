package com.leandroftm.ordermanagement.order_management_api.exception;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class DataIntegrityViolationException extends DomainException {
    public DataIntegrityViolationException(String message,  ErrorCode errorCode) {
        super(
                message,
                errorCode
        );
    }
}
