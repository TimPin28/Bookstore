package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.models.CartItem;
import com.pinawin.bookstore.models.Order;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.BookRepository;
import com.pinawin.bookstore.repositories.CartItemRepository;
import com.pinawin.bookstore.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    void checkout_successful() {

        User user = new User();
        Book book = new Book();
        book.setStock(10);
        book.setPrice(BigDecimal.valueOf(100));

        CartItem item = new CartItem();
        item.setBook(book);
        item.setQuantity(2);

        when(cartItemRepository.findByUser(user))
                .thenReturn(List.of(item));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(i -> i.getArgument(0));

        Order order = checkoutService.checkout(user);

        assertEquals(BigDecimal.valueOf(200), order.getTotalAmount());
        assertEquals(8, book.getStock());
        verify(cartItemRepository).deleteAll(anyList());
    }

    // Empty cart = RunTime Exception
    @Test
    void checkout_emptyCart_throwsException() {
        when(cartItemRepository.findByUser(any()))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class, () ->
                checkoutService.checkout(new User())
        );
    }
}

