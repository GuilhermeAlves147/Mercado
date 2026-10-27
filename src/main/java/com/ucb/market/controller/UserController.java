package com.ucb.market.controller;

import com.ucb.market.dto.*;
import com.ucb.market.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid RegisterUserRequest req) {
        return userService.register(req);
    }

    @GetMapping("/users/{id}/seller/status")
    public java.util.Map<String, Boolean> sellerStatus(@PathVariable Integer id) {
        return java.util.Map.of("seller", userService.isSeller(id));
    }

    @PostMapping("/users/{id}/seller")
    @ResponseStatus(HttpStatus.CREATED)
    public void becomeSeller(@PathVariable Integer id, @RequestBody @Valid SellerRequest req) {
        userService.becomeSeller(id, req);
    }

    @PostMapping("/users/{id}/client")
    @ResponseStatus(HttpStatus.CREATED)
    public void becomeClient(@PathVariable Integer id, @RequestBody @Valid ClientRequest req) {
        userService.becomeClient(id, req);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody @Valid LoginRequest req) {
        return userService.login(req);
    }
}