package com.example.productservice.enums;

import lombok.Getter;

@Getter
public enum ReasonBehind {
    PRODUCT_NOT_FOUND("Product not found in the inventory"),
    INSUFFICIENT_STOCK("Insufficient stock available for the requested product"),
    INVALID_PRODUCT_ID("The provided product ID is invalid or does not exist"),
    ORDER_CANCELLATION("Order was cancelled, reverting stock changes")
;
    private final String description;

    ReasonBehind(String description) {
        this.description = description;
    }

}
