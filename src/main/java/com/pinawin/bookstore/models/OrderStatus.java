package com.pinawin.bookstore.models;

import lombok.Getter;

/**
 * Enumeration representing the various stages of an order's lifecycle.
 * Used by the Order entity to track progress from placement to completion or cancellation.
 */
@Getter
public enum OrderStatus {

    /**
     * Initial state when an order is first created after a successful checkout.
     */
    PLACED,

    /**
     * State indicating that payment for the order has been successfully processed.
     */
    PAID,

    /**
     * State indicating that the items have been dispatched to the customer.
     */
    SHIPPED,

    /**
     * State indicating that the order has been voided by the user or the system.
     */
    CANCELLED;

}
