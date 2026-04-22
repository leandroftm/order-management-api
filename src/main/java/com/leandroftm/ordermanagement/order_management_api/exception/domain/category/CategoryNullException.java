package com.leandroftm.ordermanagement.order_management_api.exception.domain.category;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class CategoryNullException extends DomainException {
    public CategoryNullException() {
        super(
                "Category cannot be null",
                ErrorCode.CATEGORY_NULL
        );
    }
}
