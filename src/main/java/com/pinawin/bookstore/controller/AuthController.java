package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return userService.login(
                request.getEmail(),
                request.getPassword()
        );
    }
}

