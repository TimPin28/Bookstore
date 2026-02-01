package com.pinawin.bookstore.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing an item within a user's shopping cart.
 * This class acts as a link between a User and a Book, tracking the 
 * specific quantity of a title selected for purchase.
 */
@Entity
@Table(name = "cart_items")
@Getter
@Setter
public class CartItem {

    /**
     * Unique identifier for the cart item record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who owns this cart item.
     * Established as a Many-to-One relationship with lazy loading to 
     * optimize database performance.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The book associated with this cart item.
     * Linked via a Many-to-One relationship to the Book entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    /**
     * The number of copies of the book currently in the user's cart.
     * This value is incremented if the user adds the same book multiple times.
     */
    private int quantity;
}
