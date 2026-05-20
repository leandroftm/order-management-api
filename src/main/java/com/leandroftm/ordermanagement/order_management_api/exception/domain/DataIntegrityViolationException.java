package com.leandroftm.ordermanagement.order_management_api.exception.domain;

import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class DataIntegrityViolationException extends RuntimeException {
    private final ErrorCode errorCode;

    public DataIntegrityViolationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
