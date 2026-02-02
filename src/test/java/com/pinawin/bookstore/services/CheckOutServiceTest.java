package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.*;
import com.pinawin.bookstore.repositories.BookRepository;
import com.pinawin.bookstore.repositories.CartItemRepository;
import com.pinawin.bookstore.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CheckoutService.
 * Verifies the complex workflow of converting a cart into a finalized order.
 */
@ExtendWith(MockitoExtension.class)
public class CheckOutServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    private User testUser;
    private Book testBook;
    private List<CartItem> cartItems;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        testBook = new Book();
        testBook.setId(101L);
        testBook.setTitle("Unit Testing 101");
        testBook.setPrice(new BigDecimal("50.00"));
        testBook.setStock(10);

        CartItem item = new CartItem();
        item.setBook(testBook);
        item.setQuantity(2);
        item.setUser(testUser);

        cartItems = new ArrayList<>();
        cartItems.add(item);
    }

    @Test
    @DisplayName("Should successfully checkout when stock is available")
    void testCheckoutSuccess() {
        // Arrange
        when(cartItemRepository.findByUser(testUser)).thenReturn(cartItems);
        // Ensure the mock returns the order that was passed to save
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Order order = checkoutService.checkout(testUser);

        // Assert
        assertNotNull(order);
        assertEquals(8, testBook.getStock()); // 10 initial - 2 purchased
        assertEquals(new BigDecimal("100.00"), order.getTotalAmount()); // 50 * 2

        // Verify workflow steps
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartItemRepository, times(1)).deleteAll(cartItems);
    }

    @Test
    @DisplayName("Should throw exception if stock is insufficient")
    void testCheckoutInsufficientStock() {
        // Arrange: Try to buy 15 when only 10 are in stock
        cartItems.getFirst().setQuantity(15);
        when(cartItemRepository.findByUser(testUser)).thenReturn(cartItems);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            checkoutService.checkout(testUser);
        });

        assertTrue(ex.getMessage().contains("Not enough stock"));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw exception when attempting to checkout an empty cart")
    void testCheckoutEmptyCart() {
        // Arrange: Mock the repository to return an empty list for this user
        when(cartItemRepository.findByUser(testUser)).thenReturn(new ArrayList<>());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            checkoutService.checkout(testUser);
        });

        // Verify the error message matches the business logic in CheckoutService
        assertEquals("Cart is empty", ex.getMessage());

        // Safety Check: Ensure no order was saved and nothing was deleted
        verify(orderRepository, never()).save(any(Order.class));
        verify(cartItemRepository, never()).deleteAll(any());
    }

    @Test
    @DisplayName("Should allow checkout when quantity matches exactly the remaining stock")
    void testCheckout_ExactStockMatch() {
        // Arrange: User wants 10, stock is exactly 10
        testBook.setStock(10);
        cartItems.getFirst().setQuantity(10);

        when(cartItemRepository.findByUser(testUser)).thenReturn(cartItems);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        checkoutService.checkout(testUser);

        // Assert
        assertEquals(0, testBook.getStock(), "Stock should be exactly zero after purchase.");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should correctly process multiple different books in one checkout")
    void testCheckout_MultipleItems() {
        // Arrange: Add a second book to the cart
        Book book2 = new Book();
        book2.setPrice(new BigDecimal("20.00"));
        book2.setStock(5);

        CartItem item2 = new CartItem();
        item2.setBook(book2);
        item2.setQuantity(1);

        cartItems.add(item2); // Now cart has testBook ($50 x 2) and book2 ($20 x 1)

        when(cartItemRepository.findByUser(testUser)).thenReturn(cartItems);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Order order = checkoutService.checkout(testUser);

        // Assert
        assertEquals(new BigDecimal("120.00"), order.getTotalAmount()); // (50*2) + 20
        assertEquals(8, testBook.getStock());
        assertEquals(4, book2.getStock());
    }
}