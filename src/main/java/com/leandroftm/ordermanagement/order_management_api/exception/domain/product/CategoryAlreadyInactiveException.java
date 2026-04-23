package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class CategoryAlreadyInactiveException extends DomainException {
    public CategoryAlreadyInactiveException() {
        super(
                "Category already inactive",
                ErrorCode.CATEGORY_ALREADY_INACTIVE
        );
    }
}
