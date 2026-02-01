package com.pinawin.bookstore.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object representing an individual item within an order.
 * It provides a simplified view of the OrderItem entity for the frontend.
 */
@Getter
@Setter
public class OrderItemResponse {

    /**
     * The title of the book at the time it was ordered.
     */
    private String bookTitle;

    /**
     * The number of copies purchased.
     */
    private int quantity;

    /**
     * The price of the book at the specific time of purchase.
     */
    private BigDecimal price;

    /**
     * Constructs a new OrderItemResponse.
     * @param bookTitle Title of the book.
     * @param quantity Number of units.
     * @param price Price per unit.
     */
    public OrderItemResponse(String bookTitle, int quantity, BigDecimal price) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
    }
}
