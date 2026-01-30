package com.pinawin.bookstore.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    private Long orderId;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;

    public OrderResponse(Long orderId,
                    BigDecimal totalAmount,
                    List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.items = items;
    }
}

