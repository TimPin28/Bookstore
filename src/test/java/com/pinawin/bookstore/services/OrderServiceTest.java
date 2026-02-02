package com.pinawin.bookstore.services;

import com.pinawin.bookstore.DTO.OrderResponse;
import com.pinawin.bookstore.models.*;
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
 * Unit tests for OrderService.
 * Focuses on the retrieval of order history and the correct mapping of
 * Order entities to OrderResponse DTOs.
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private User testUser;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);

        // Setup a mock Order with nested OrderItems
        testOrder = new Order();
        testOrder.setId(500L);
        testOrder.setUser(testUser);
        testOrder.setStatus(OrderStatus.PLACED);
        testOrder.setTotalAmount(new BigDecimal("150.00"));

        OrderItem item = new OrderItem();
        Book book = new Book();
        book.setTitle("Mastering Java");
        item.setBook(book);
        item.setQuantity(2);
        item.setPrice(new BigDecimal("75.00"));

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        testOrder.setOrderItems(items);
    }

    @Test
    @DisplayName("Should return a list of OrderResponse DTOs for a user")
    void testGetUserOrdersMapping() {
        // Arrange: Mock repository to return the entity list
        when(orderRepository.findByUser(testUser)).thenReturn(List.of(testOrder));

        // Act: Call service method
        List<OrderResponse> result = orderService.getOrdersForUser(testUser);

        // Assert: Verify DTO mapping accuracy
        assertNotNull(result);
        assertEquals(1, result.size());

        OrderResponse response = result.getFirst();
        assertEquals(500L, response.getOrderId());
        assertEquals("PLACED", response.getStatus());
        assertEquals(new BigDecimal("150.00"), response.getTotalAmount());

        // Verify nested item mapping (Nested Loop check)
        assertEquals(1, response.getItems().size());
        assertEquals("Mastering Java", response.getItems().getFirst().getBookTitle());

        verify(orderRepository, times(1)).findByUser(testUser);
    }

    @Test
    @DisplayName("Should return an empty list when user has no orders")
    void testGetUserOrdersEmpty() {
        // Arrange
        when(orderRepository.findByUser(testUser)).thenReturn(new ArrayList<>());

        // Act
        List<OrderResponse> result = orderService.getOrdersForUser(testUser);

        // Assert
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findByUser(testUser);
    }
}