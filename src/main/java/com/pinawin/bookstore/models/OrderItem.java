package com.pinawin.bookstore.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


/**
 * Entity representing an individual line item within a finalized Order.
 * Unlike CartItem, this entity preserves the price of the book at the 
 * exact moment of purchase to ensure historical accuracy.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {

    /**
     * Unique identifier for the order item record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The parent order that this item belongs to.
     * Established with a Many-to-One relationship and lazy loading 
     * for performance optimization.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * The book associated with this specific order line.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    /**
     * The quantity of the book purchased in this transaction.
     */
    private int quantity;

    /**
     * The price of the book captured during the checkout process.
     * This ensures that future changes to a book's catalog price do 
     * not retroactively alter past order records.
     */
    private BigDecimal price;
}

