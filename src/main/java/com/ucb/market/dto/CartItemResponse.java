package com.ucb.market.dto;

import com.ucb.market.model.BuyItem;

import java.math.BigDecimal;

public record CartItemResponse(Integer productId, String productName, Integer quantity,
                               BigDecimal unitValue, BigDecimal subtotal) {
    public static CartItemResponse from(BuyItem i) {
        return new CartItemResponse(i.getProduct().getId(), i.getProduct().getName(),
                i.getQuantity(), i.getUnitValue(),
                i.getUnitValue().multiply(BigDecimal.valueOf(i.getQuantity())));
    }
}
