package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class NullProductException extends DomainException {
    public NullProductException() {
        super(
                "Product cannot be null",
                ErrorCode.PRODUCT_NULL
        );
    }
}
