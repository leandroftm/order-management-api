package com.leandroftm.ordermanagement.order_management_api.exception.domain.category;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class CategoryStatusNullException extends DomainException {
    public CategoryStatusNullException() {
        super(
                "Category status cannot be null",
                ErrorCode.CATEGORY_STATUS_NULL
        );
    }
}
