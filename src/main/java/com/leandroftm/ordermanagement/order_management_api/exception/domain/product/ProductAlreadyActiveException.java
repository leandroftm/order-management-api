package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class ProductAlreadyActiveException extends DomainException {
    public ProductAlreadyActiveException() {
        super(
                "Product already active",
                ErrorCode.PRODUCT_ALREADY_ACTIVE
        );
    }
}
