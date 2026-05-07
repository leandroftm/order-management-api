package com.leandroftm.ordermanagement.order_management_api.domain.dto.request.update;

import java.math.BigDecimal;

public record UpdatePriceRequest(
        BigDecimal price
) {
}
