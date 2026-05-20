package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DataIntegrityViolationException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class ProductAlreadyExistsException extends DataIntegrityViolationException {
    public ProductAlreadyExistsException() {
        super(
                "Product already exists",
                ErrorCode.PRODUCT_ALREADY_EXISTS
        );
    }
}
