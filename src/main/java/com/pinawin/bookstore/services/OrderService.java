package com.pinawin.bookstore.services;

import com.pinawin.bookstore.DTO.OrderItemResponse;
import com.pinawin.bookstore.DTO.OrderResponse;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderResponse> getOrdersForUser(User user) {

        return orderRepository.findByUser(user).stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getStatus().name(),
                        order.getTotalAmount(),
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


