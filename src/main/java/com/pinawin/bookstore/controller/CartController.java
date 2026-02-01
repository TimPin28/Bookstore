package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.models.CartItem;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing shopping cart operations.
 * Allows authenticated users to add books to their cart and view their current items.
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    private final CartService cartService;

    /**
     * Constructor-based dependency injection for CartService.
     * @param cartService The service layer handling cart business logic.
     */
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Adds a book to the authenticated user's shopping cart.
     * Requires the user to be logged in to access the security principal.
     * @param bookId The ID of the book to be added.
     * @param authentication The current security context injected by Spring Security.
     * @return ResponseEntity with the saved CartItem or an unauthorized/error status.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam Long bookId,
            Authentication authentication
    ) {
        // 1. Verify that the user is authenticated via the session
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first");
        }

        try {
            // 2. Cast the authenticated principal to the User model to associate with the cart item
            User user = (User) authentication.getPrincipal();
            CartItem savedItem = cartService.addToCart(user, bookId);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            // Returns a 500 status if the book is not found or a database error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not add to cart");
        }
    }

    /**
     * Retrieves the list of items currently in the authenticated user's shopping cart.
     * @param authentication The current security context.
     * @return A list of CartItem entities belonging to the user.
     */
    @GetMapping
    public List<CartItem> viewCart(Authentication authentication) {
        // Extract the User principal from the security context
        User user = (User) authentication.getPrincipal();
        
        return cartService.getCart(user);
    }
}