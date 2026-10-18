package com.ucb.market.dto;

import com.ucb.market.model.Cart;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(Integer id, Integer clientId, BigDecimal totalValue,
                           Integer itemQuantity, List<CartItemResponse> items) {
    public static CartResponse from(Cart c) {
        return new CartResponse(c.getId(), c.getClient().getId(), c.getTotalValue(),
                c.getItemQuantity(), c.getItems().stream().map(CartItemResponse::from).toList());
    }
}
