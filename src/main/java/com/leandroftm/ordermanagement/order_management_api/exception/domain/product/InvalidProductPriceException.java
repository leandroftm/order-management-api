package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidProductPriceException extends DomainException {
    public InvalidProductPriceException() {
        super(
                "Invalid product price value",
                ErrorCode.INVALID_PRODUCT_PRICE
        );
    }
}
