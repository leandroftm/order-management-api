package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InsufficientStockException extends DomainException {
    public InsufficientStockException() {
        super(
                "Insufficient stock amount",
                ErrorCode.INSUFFICIENT_STOCK
        );
    }
}
