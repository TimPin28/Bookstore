package com.pinawin.bookstore.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object for sending order details to the frontend.
 * This class provides a flattened view of the Order entity, including 
 * itemized details for display in the user's order history.
 */
@Getter
@Setter
public class OrderResponse {

    /**
     * The unique identifier for the order record.
     */
    private Long orderId;

    /**
     * The current processing state of the order (e.g., PLACED, SHIPPED).
     */
    private String status;

    /**
     * The total monetary value of the order.
     */
    private BigDecimal totalAmount;

    /**
     * A list of individual items included in this order.
     */
    private List<OrderItemResponse> items;

    /**
     * Constructs a new OrderResponse with itemized details.
     * @param orderId Primary key of the order.
     * @param status String representation of the OrderStatus.
     * @param totalAmount Total cost.
     * @param items List of DTOs representing the books purchased.
     */
    public OrderResponse(Long orderId,
                    String status,
                    BigDecimal totalAmount,
                    List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.items = items;
    }
}

