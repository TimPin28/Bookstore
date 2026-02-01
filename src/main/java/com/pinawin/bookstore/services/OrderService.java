package com.pinawin.bookstore.services;

import com.pinawin.bookstore.DTO.OrderItemResponse;
import com.pinawin.bookstore.DTO.OrderResponse;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for retrieving and formatting order history.
 * Handles the transformation of Order entities into Response DTOs.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Retrieves all orders for a specific user and maps them to OrderResponse DTOs.
     * Uses Java Streams to iterate through entities and create a sanitized data transfer object.
     * @param user The authenticated user whose orders are being fetched.
     * @return A list of formatted OrderResponse objects for the profile UI.
     */
    public List<OrderResponse> getOrdersForUser(User user) {
        // Fetch raw entities from the database
        return orderRepository.findByUser(user).stream()
                // Convert Entities to DTOs
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getStatus().name(),
                        order.getTotalAmount(),
                        // Map the nested list of OrderItems to OrderItemResponse DTOs
                        order.getOrderItems().stream()
                                .map(item -> new OrderItemResponse(
                                        item.getBook().getTitle(),
                                        item.getQuantity(),
                                        item.getPrice()
                                ))
                                .toList()
                ))
                .toList();
    }
}


