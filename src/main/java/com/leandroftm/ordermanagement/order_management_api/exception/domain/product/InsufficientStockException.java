package com.leandroftm.ordermanagement.order_management_api.exception.domain.product;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;

public class InsuficientStockException extends DomainException {
    public InsuficientStockException(String message) {
        super(
                "Insufficient stock",
                
        );
    }
}
