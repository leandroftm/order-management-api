package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class InvalidStockAmountException extends DomainException {
    public InvalidStockAmountException() {
        super(
                "Invalid stock amount value",
                ErrorCode.INVALID_STOCK_AMOUNT_VALUE
        );
    }
}
