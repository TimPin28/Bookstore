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
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemResponse> items;

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

