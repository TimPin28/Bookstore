package com.pinawin.bookstore.controller;

import com.pinawin.bookstore.DTO.OrderResponse;
import com.pinawin.bookstore.models.Order;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.services.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing user order history.
 * Provides endpoints for authenticated users to retrieve their past purchases.
 */
@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructor-based dependency injection for OrderService.
     * @param orderService The service layer handling order data retrieval and mapping.
     */
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves a list of orders belonging specifically to the currently logged-in user.
     * Uses the Authentication object to ensure users can only see their own history.
     * @param authentication The current security context injected by Spring Security.
     * @return A list of OrderResponse DTOs containing order details and itemized lists.
     */
    @GetMapping
    public List<OrderResponse> getMyOrders(Authentication authentication) {
        // Extract the User principal from the security context
        User user = (User) authentication.getPrincipal();
        
        // Delegate to the service layer to fetch and map order data
        return orderService.getOrdersForUser(user);
    }
}

