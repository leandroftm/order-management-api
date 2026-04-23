package com.leandroftm.ordermanagement.order_management_api.exception.domain.category;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidCategoryNameException extends DomainException {
    public InvalidCategoryNameException() {
        super(
                "Invalid category name",
                ErrorCode.INVALID_CATEGORY_NAME
        );
    }
}
