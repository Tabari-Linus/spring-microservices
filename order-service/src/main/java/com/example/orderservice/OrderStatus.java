package com.example.orderservice;

public enum OrderStatus {
    PENDING,
    COMPLETED,
    CANCELLED,
    FAILED;

    public static OrderStatus fromString(String status) {
        try {
            return OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown order status: " + status);
        }
    }
}
