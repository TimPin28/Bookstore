package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.models.CartItem;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.BookRepository;
import com.pinawin.bookstore.repositories.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CartService.
 * Focuses on business rules for adding items and managing user-specific carts.
 */
@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartService cartService;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUserName("Timothy");

        testBook = new Book();
        testBook.setId(101L);
        testBook.setTitle("Spring Boot Guide");
        testBook.setStock(10);
    }

    @Test
    @DisplayName("Should add a new item to the cart when it doesn't exist in the cart")
    void testAddToCartNewItem() {
        // Arrange
        when(bookRepository.findById(101L)).thenReturn(Optional.of(testBook));
        when(cartItemRepository.findByUserAndBook(testUser, testBook)).thenReturn(Optional.empty());

        // Act
        cartService.addToCart(testUser, 101L);

        // Assert
        // Verify that a new CartItem was saved with quantity 1
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should increment quantity when item already exists in cart")
    void testAddToCartIncrementQuantity() {
        // Arrange
        CartItem existingItem = new CartItem();
        existingItem.setUser(testUser);
        existingItem.setBook(testBook);
        existingItem.setQuantity(1);

        when(bookRepository.findById(101L)).thenReturn(Optional.of(testBook));
        when(cartItemRepository.findByUserAndBook(testUser, testBook)).thenReturn(Optional.of(existingItem));

        // Act
        cartService.addToCart(testUser, 101L);

        // Assert
        assertEquals(2, existingItem.getQuantity());
        verify(cartItemRepository, times(1)).save(existingItem);
    }

    @Test
    @DisplayName("Should return all cart items for a specific user")
    void testGetCartItems() {
        // Arrange
        List<CartItem> mockItems = List.of(new CartItem(), new CartItem());
        when(cartItemRepository.findByUser(testUser)).thenReturn(mockItems);

        // Act
        List<CartItem> result = cartService.getCart(testUser);

        // Assert
        assertEquals(2, result.size());
        verify(cartItemRepository, times(1)).findByUser(testUser);
    }

    @Test
    @DisplayName("Should call repository to delete all items for a user")
    void testClearCart() {
        // Act
        // We pass the testUser created in the @BeforeEach setUp method
        cartService.clearCart(testUser);

        // Assert
        // Verify that the repository method was called exactly once
        verify(cartItemRepository, times(1)).deleteAllByUser(testUser);
    }

    @Test
    @DisplayName("Should block addition if stock is exactly 0 (matches UI 'Out of Stock' state)")
    void testAddToCart_ZeroStock() {
        // Arrange: Stock is 0, just like your UI shows
        testBook.setStock(0);
        when(bookRepository.findById(101L)).thenReturn(Optional.of(testBook));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cartService.addToCart(testUser, 101L);
        });

        assertEquals("This book is currently out of stock.", ex.getMessage());
        verify(cartItemRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should allow adding if stock is 1 (the minimum visible state)")
    void testAddToCart_MinimumStock() {
        // Arrange: Stock is 1, button is still visible in UI
        testBook.setStock(1);
        when(bookRepository.findById(101L)).thenReturn(Optional.of(testBook));
        when(cartItemRepository.findByUserAndBook(testUser, testBook)).thenReturn(Optional.empty());

        // Act
        cartService.addToCart(testUser, 101L);

        // Assert
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Should successfully clear all items for a specific user")
    void testClearCart_Successful() {
        // Act: Use the testUser from @BeforeEach
        cartService.clearCart(testUser);

        // Assert: Verify that the exact repository method was called for this specific user
        verify(cartItemRepository, times(1)).deleteAllByUser(testUser);

        // Extra Guard: Verify we didn't call the general deleteAll method by mistake
        verify(cartItemRepository, never()).deleteAll();
    }
}