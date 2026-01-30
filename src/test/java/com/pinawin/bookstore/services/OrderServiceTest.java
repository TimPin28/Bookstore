package com.pinawin.bookstore.services;

import com.pinawin.bookstore.DTO.OrderResponse;
import com.pinawin.bookstore.models.*;
import com.pinawin.bookstore.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getOrdersForUser_returnsOrderResponses() {

        User user = new User();

        Book book = new Book();
        book.setTitle("Clean Code");

        OrderItem item = new OrderItem();
        item.setBook(book);
        item.setQuantity(1);
        item.setPrice(BigDecimal.valueOf(50));

        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PLACED);
        order.setTotalAmount(BigDecimal.valueOf(50));
        order.setOrderItems(List.of(item));

        when(orderRepository.findByUser(user))
                .thenReturn(List.of(order));

        List<OrderResponse> responses =
                orderService.getOrdersForUser(user);

        assertEquals(1, responses.size());
        assertEquals("Clean Code",
                responses.get(0).getItems().get(0).getBookTitle());
    }
}

