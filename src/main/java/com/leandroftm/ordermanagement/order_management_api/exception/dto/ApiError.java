package com.leandroftm.ordermanagement.order_management_api.exception.dto;

import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        int status,
        String error,
        ErrorCode errorCode,
        List<String> messages,
        String path,
        LocalDateTime timestamp
) {
}
