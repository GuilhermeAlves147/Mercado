package com.ucb.market.controller;

import com.ucb.market.dto.UserDTO;
import com.ucb.market.model.User;
import com.ucb.market.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User register(@RequestBody @Valid UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserDTO userDTO) {

        User user = userService.login(userDTO);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(user);
    }
}