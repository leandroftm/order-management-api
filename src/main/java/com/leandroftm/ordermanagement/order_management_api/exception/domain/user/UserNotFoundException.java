package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.NotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super(
                "User not found",
                ErrorCode.USER_NOT_FOUND
        );
    }
}
