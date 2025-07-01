package com.example.orderservice.exception;

public class OrderNotFoundException extends RuntimeException {

    private final Long orderId;

    public OrderNotFoundException(Long orderId) {
        super("Order with ID: " + orderId + " not found");
        this.orderId = orderId;
    }
}
