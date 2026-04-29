package com.leandroftm.ordermanagement.order_management_api.exception.domain.category;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class CategoryAlreadyActiveException extends DomainException {
    public CategoryAlreadyActiveException() {
        super(
                "Category already active",
                ErrorCode.CATEGORY_ALREADY_ACTIVE
        );
    }
}
