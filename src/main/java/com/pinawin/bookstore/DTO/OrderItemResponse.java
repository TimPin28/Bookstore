package com.pinawin.bookstore.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {

    private String bookTitle;
    private int quantity;
    private BigDecimal price;

    public OrderItemResponse(String bookTitle, int quantity, BigDecimal price) {
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.price = price;
    }
}
