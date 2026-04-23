package com.leandroftm.ordermanagement.order_management_api.exception.domain.category;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class CategoryNotEmptyException extends DomainException {
    public CategoryNotEmptyException() {
        super(
                "Category is not empty",
                ErrorCode.CATEGORY_NOT_EMPTY
        );
    }
}
