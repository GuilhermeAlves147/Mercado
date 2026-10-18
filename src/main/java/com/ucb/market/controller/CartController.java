package com.ucb.market.controller;

import com.ucb.market.dto.AddCartItemRequest;
import com.ucb.market.dto.CartResponse;
import com.ucb.market.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/clients/{clientId}/cart")
    public CartResponse getByClient(@PathVariable Integer clientId) {
        return cartService.getByClient(clientId);
    }

    @PostMapping("/carts/{cartId}/items")
    public CartResponse addItem(@PathVariable Integer cartId,
                                @RequestBody @Valid AddCartItemRequest req) {
        return cartService.addItem(cartId, req);
    }

    @DeleteMapping("/carts/{cartId}/items/{productId}")
    public CartResponse removeItem(@PathVariable Integer cartId, @PathVariable Integer productId) {
        return cartService.removeItem(cartId, productId);
    }
}