package com.ucb.market.dto;

import com.ucb.market.model.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(Integer productId, String productName, Integer quantity,
                                BigDecimal unitValue) {
    public static OrderItemResponse from(OrderItem i) {
        return new OrderItemResponse(i.getProduct().getId(), i.getProduct().getName(),
                i.getQuantity(), i.getUnitValue());
    }
}
