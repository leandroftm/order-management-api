package com.leandroftm.ordermanagement.order_management_api.exception.domain.order;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;

public class OrderItemsListIsEmptyException extends DomainException {
    public OrderItemsListIsEmptyException() {
        super(
                "Order has no items",
                ErrorCode.ORDER_ITEMS_LIST_IS_EMPTY
        );
    }
}
