package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.models.Order;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.services.CheckoutService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
@CrossOrigin
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public Order checkout(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return checkoutService.checkout(user);
    }
}

