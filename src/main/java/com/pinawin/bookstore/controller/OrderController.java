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

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponse> getMyOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrdersForUser(user);
    }
}

