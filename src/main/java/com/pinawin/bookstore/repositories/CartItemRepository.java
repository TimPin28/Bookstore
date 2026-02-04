package com.pinawin.bookstore.repositories;

import com.pinawin.bookstore.models.Book;
import com.pinawin.bookstore.models.CartItem;
import com.pinawin.bookstore.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CartItem entities.
 * Provides specialized methods to retrieve and manage items within 
 * a user's persistent shopping cart.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * Retrieves a specific cart item based on the user and the book.
     * Used by the CartService to determine if a quantity should be 
     * incremented or a new record should be created.
     * @param user The owner of the cart.
     * @param book The book being added to the cart.
     * @return An Optional containing the CartItem if found.
     */
    Optional<CartItem> findByUserAndBook(User user, Book book);

    /**
     * Retrieves all cart items belonging to a specific user.
     * Supports the "View Cart" and "Checkout" features.
     * @param user The user whose cart is being retrieved.
     * @return A list of CartItem entities associated with the user.
     */
    List<CartItem> findByUser(User user);

    /**
     * Removes all shopping cart items associated with a specific user.
     * @param user the user entity whose cart items should be deleted.
     */
    @Modifying
    @Transactional
    void deleteAllByUser(User user);
}

