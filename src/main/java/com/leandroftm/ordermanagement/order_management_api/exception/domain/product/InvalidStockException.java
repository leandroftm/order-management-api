package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidStockException extends DomainException {
    public InvalidStockException() {
        super(
                "Invalid stock value",
                ErrorCode.INVALID_STOCK_VALUE
        );
    }
}
