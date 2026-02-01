package com.pinawin.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Entity representing a book in the system.
 * This class is mapped to the "books" table in the MySQL database and 
 * serves as the primary data model for the catalog.
 */
@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    /**
     * Unique identifier for the book.
     * Automatically generated using the identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The full title of the book.
     */
    private String title;

    /**
     * The name of the book's author.
     */
    private String author;

    /**
     * A detailed summary or description of the book.
     * Configured with a length of 2000 characters to support longer text.
     */
    @Column(length = 2000)
    private String description;

    /**
     * The unit price of the book.
     */
    private BigDecimal price;

    /**
     * The genre or category the book belongs to (e.g., Fiction, Tech).
     */
    private String category;

    /**
     * The current number of copies available in the inventory.
     * This value is decremented during the checkout process.
     */
    private int stock;

    /**
     * Relationship to the order items containing this book.
     * JsonIgnore is used to prevent infinite recursion during JSON serialization.
     */
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderItem> orderItems;

    /**
     * Relationship to cart items where this book has been added.
     * Fetched lazily to optimize database performance.
     */
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CartItem> cartItems;
}