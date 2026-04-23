package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class ProductAlreadyInactiveException extends DomainException {
    public ProductAlreadyInactiveException() {
        super(
                "Product already inactive",
                ErrorCode.PRODUCT_ALREADY_INACTIVE
        );
    }
}
