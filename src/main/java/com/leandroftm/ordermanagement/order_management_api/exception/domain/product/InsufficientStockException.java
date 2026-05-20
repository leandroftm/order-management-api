package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InsuficientStockException extends DomainException {
    public InsuficientStockException() {
        super(
                "Insufficient stock amount",
                ErrorCode.INSUFFICIENT_STOCK
        );
    }
}
