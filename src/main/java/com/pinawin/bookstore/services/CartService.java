package com.pinawin.bookstore.services;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.models.CartItem;
import com.pinawin.bookstore.models.User;
import com.pinawin.bookstore.repositories.BookRepository;
import com.pinawin.bookstore.repositories.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing shopping cart business logic.
 * Handles the addition of books to a user's cart and retrieval of cart contents.
 */
@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    /**
     * Constructor for dependency injection.
     * @param cartItemRepository Repository for cart item data access.
     * @param bookRepository Repository for book data access.
     */
    public CartService(CartItemRepository cartItemRepository,
                       BookRepository bookRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Adds a book to a user's shopping cart.
     * If the book is already present in the cart, the quantity is incremented by 1.
     * Otherwise, a new CartItem record is created.
     * @param user The user adding the book.
     * @param bookId The ID of the book to add.
     * @return The saved or updated CartItem.
     * @throws RuntimeException if the book is not found.
     */
    public CartItem addToCart(User user, Long bookId) {

        // Retrieve book details or throw exception if not found
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if the book is already in the user's cart
        CartItem cartItem = cartItemRepository
                .findByUserAndBook(user, book)
                .orElse(null);

        if (cartItem == null) {
            // Create a new entry if the book wasn't in the cart
            cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setBook(book);
            cartItem.setQuantity(1);
        } else {
            // Increment quantity if book already exists in the cart
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }

        return cartItemRepository.save(cartItem);
    }

    /**
     * Retrieves all cart items for a specific user.
     * @param user The user whose cart is being retrieved.
     * @return A list of CartItem entities.
     */
    public List<CartItem> getCart(User user) {
        return cartItemRepository.findByUser(user);
    }
}
