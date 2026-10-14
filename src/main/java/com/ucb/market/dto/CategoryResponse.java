package com.ucb.market.dto;

import com.ucb.market.model.Category;

public record CategoryResponse(Integer id, String name, String description, Integer parentId) {
    public static CategoryResponse from(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getDescription(),
                c.getParent() == null ? null : c.getParent().getId());
    }
}
