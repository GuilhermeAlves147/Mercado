package com.ucb.market.dto;

import com.ucb.market.model.User;

public record UserResponse(Integer id, String username, String email) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getEmail());
    }
}
