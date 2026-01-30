package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.*;
import com.pinawin.bookstore.repositories.BookRepository;
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
    private final BookRepository bookRepository;

    public CheckoutService(CartItemRepository cartItemRepository,
                           OrderRepository orderRepository,
                           BookRepository bookRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
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

            Book book = cartItem.getBook();

            // check book stock
            if (book.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Not enough stock for book: " + book.getTitle()
                );
            }

            // reduce stock if there is stock
            book.setStock(book.getStock() - cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(book.getPrice());

            order.getOrderItems().add(orderItem);

            total = total.add(
                    book.getPrice()
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


