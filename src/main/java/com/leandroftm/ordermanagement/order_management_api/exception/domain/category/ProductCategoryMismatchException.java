package com.leandroftm.ordermanagement.order_management_api.exception.domain.category;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class ProductCategoryMismatchException extends DomainException {
    public ProductCategoryMismatchException() {
        super(
                "Product and category mismatch",
                ErrorCode.PRODUCT_CATEGORY_MISMATCH
        );
    }
}
