package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.models.CartItem;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Long bookId,
            Authentication authentication
    ) {
        // 1. Check for null authentication
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first");
        }

        try {
            // 2. Cast the principal to User model
            User user = (User) authentication.getPrincipal();
            CartItem savedItem = cartService.addToCart(user, bookId);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add to cart");
        }
    }

    @GetMapping
    public List<CartItem> viewCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartService.getCart(user);
    }
}