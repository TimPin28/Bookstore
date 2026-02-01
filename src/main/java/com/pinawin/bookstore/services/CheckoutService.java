package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.*;
import com.pinawin.bookstore.repositories.BookRepository;
import com.pinawin.bookstore.repositories.CartItemRepository;
import com.pinawin.bookstore.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class responsible for the checkout process.
 * This service handles the transition from a temporary shopping cart 
 * to a permanent order, ensuring stock levels are updated correctly.
 */
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

    /**
     * Executes the checkout transaction for a given user.
     * The @Transactional annotation ensures that if any part of the process fails 
     * (e.g., insufficient stock), all changes are rolled back.
     * @param user The authenticated user performing the checkout.
     * @return The saved Order entity.
     * @throws RuntimeException if the cart is empty or requested stock is unavailable.
     */
    @Transactional
    public Order checkout(User user) {

        // 1. Retrieve all items currently in the user's cart
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Initialize a new Order entity
        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;

        // 3. Process each item in the cart
        for (CartItem cartItem : cartItems) {

            Book book = cartItem.getBook();

            // Validate inventory: Check if enough stock exists for the requested quantity
            if (book.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Not enough stock for book: " + book.getTitle()
                );
            }

            // Update inventory: Reduce stock immediately
            book.setStock(book.getStock() - cartItem.getQuantity());

            // Create a snapshot of the item for the order history
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(book.getPrice());

            order.getOrderItems().add(orderItem);

            // Accumulate the total cost
            total = total.add(
                    book.getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
            );
        }

        // 4. Finalize order details
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.PLACED);

        // 5. Persist the order and associated order items to the database
        Order savedOrder = orderRepository.save(order);

        // 6. Clear the user's shopping cart upon successful order placement
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }
}


