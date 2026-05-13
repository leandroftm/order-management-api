package com.leandroftm.ordermanagement.order_management_api.exception.domain.user;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class CannotDisableAdminException extends DomainException {
    public CannotDisableAdminException() {
        super(
                "Invalid role transition",
                ErrorCode.CANNOT_DISABLE_ADMIN
        );
    }
}
