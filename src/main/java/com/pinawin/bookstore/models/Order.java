package com.pinawin.bookstore.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a finalized customer order.
 * This class persists the transaction details, including the total cost, 
 * current status, and the specific items associated with the purchase.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    /**
     * Unique identifier for the order.
     * Automatically generated using the database's identity column.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who placed the order.
     * Linked via a Many-to-One relationship. Fetched lazily to improve 
     * efficiency when retrieving order lists.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The total monetary value of the transaction at the time of checkout.
     */
    private BigDecimal totalAmount;

    /**
     * The current lifecycle state of the order (ORDERED, PLACED, PAID, SHIPPED).
     * Stored as a string in the database for better readability.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    /**
     * The list of specific books and quantities included in this order.
     * Uses CascadeType.ALL to ensure that when an order is saved, 
     * its associated items are also persisted.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
}
