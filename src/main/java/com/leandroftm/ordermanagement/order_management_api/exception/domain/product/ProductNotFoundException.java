package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.NotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException() {
        super(
                "Product not found",
                ErrorCode.PRODUCT_NOT_FOUND
        );
    }
}
