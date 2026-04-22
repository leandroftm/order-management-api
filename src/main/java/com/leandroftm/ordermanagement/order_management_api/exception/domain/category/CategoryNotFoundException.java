package com.leandroftm.ordermanagement.order_management_api.exception.domain.category;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.NotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException() {
        super(
                "Category not found",
                ErrorCode.CATEGORY_NOT_FOUND
        );
    }
}
