package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.models.CartItem;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.BookRepository;
import com.pinawin.bookstore.repositories.CartItemRepository;
import com.pinawin.bookstore.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    public CartService(CartItemRepository cartItemRepository,
                       BookRepository bookRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    public CartItem addToCart(User user, Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        CartItem cartItem = cartItemRepository
                .findByUserAndBook(user, book)
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setBook(book);
            cartItem.setQuantity(1);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }

        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCart(User user) {
        return cartItemRepository.findByUser(user);
    }
}
