package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.models.Order;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.services.CheckoutService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for handling the checkout process.
 * Acts as the entry point for converting a user's temporary shopping cart 
 * into a permanent order record.
 */
@RestController
@RequestMapping("/api/checkout")
@CrossOrigin
public class CheckoutController {

    private final CheckoutService checkoutService;

    /**
     * Constructor-based dependency injection for CheckoutService.
     * @param checkoutService The service layer containing the transactional checkout logic.
     */
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    /**
     * Processes the checkout for the currently authenticated user.
     * Retrieves the user principal from the security context to identify the cart owner.
     * @param authentication The current security context injected by Spring Security.
     * @return The finalized Order entity containing order items and total amount.
     */
    @PostMapping
    public Order checkout(Authentication authentication) {
        // Extracts the User object from the session's authentication principal
        User user = (User) authentication.getPrincipal();

        // Delegates the transactional business logic to the CheckoutService
        return checkoutService.checkout(user);
    }
}

