package com.leandroftm.ordermanagement.order_management_api.exception.domain.category;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class CategoryAlreadyExistsException extends DomainException {
    public CategoryAlreadyExistsException() {
        super(
                "Category already exists",
                ErrorCode.CATEGORY_ALREADY_EXISTS
        );
    }
}
