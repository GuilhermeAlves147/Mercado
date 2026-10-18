package com.ucb.market.controller;

import com.ucb.market.dto.ChangeStatusRequest;
import com.ucb.market.dto.OrderResponse;
import com.ucb.market.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/carts/{cartId}/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(@PathVariable Integer cartId) {
        return orderService.checkout(cartId);
    }

    @GetMapping("/orders/{id}")
    public OrderResponse findById(@PathVariable Integer id) {
        return orderService.findById(id);
    }

    @GetMapping("/clients/{clientId}/orders")
    public List<OrderResponse> listByClient(@PathVariable Integer clientId) {
        return orderService.listByClient(clientId);
    }

    @PatchMapping("/orders/{id}/status")
    public OrderResponse changeStatus(@PathVariable Integer id,
                                      @RequestBody @Valid ChangeStatusRequest req) {
        return orderService.changeStatus(id, req.status());
    }
}
