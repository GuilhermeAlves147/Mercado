package com.ucb.market.dto;

import com.ucb.market.model.Product;

import java.math.BigDecimal;

public record ProductResponse(Integer id, String name, BigDecimal value, Integer quantity,
                              Integer sellerId, Integer categoryId, String categoryName) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getValue(), p.getQuantity(),
                p.getSeller().getId(), p.getCategory().getId(), p.getCategory().getName());
    }
}
