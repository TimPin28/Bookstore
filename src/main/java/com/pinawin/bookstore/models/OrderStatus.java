package com.pinawin.bookstore.models;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PLACED,
    PAID,
    SHIPPED,
    CANCELLED;

}
