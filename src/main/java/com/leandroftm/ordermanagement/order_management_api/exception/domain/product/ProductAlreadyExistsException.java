package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class ProductAlreadyExistsException extends DomainException {
    public ProductAlreadyExistsException() {
        super(
                "Product already exists",
                ErrorCode.PRODUCT_ALREADY_EXISTS
        );
    }
}
