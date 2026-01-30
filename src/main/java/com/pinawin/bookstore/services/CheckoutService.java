package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.CartItem;
import com.pinawin.bookstore.models.Order;
import com.pinawin.bookstore.models.OrderItem;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.CartItemRepository;
import com.pinawin.bookstore.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CheckoutService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public CheckoutService(CartItemRepository cartItemRepository,
                           OrderRepository orderRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order checkout(User user) {

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());

            order.getOrderItems().add(orderItem);

            total = total.add(
                    cartItem.getBook().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        // clear cart after successful checkout
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }
}

