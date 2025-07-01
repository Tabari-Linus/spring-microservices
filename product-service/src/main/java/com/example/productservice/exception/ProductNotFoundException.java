package com.example.productservice.exception;

import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
    private final long productId;

    public ProductNotFoundException(long productId) {
        super("Product with ID: " + productId + " not found");
        this.productId = productId;
    }
}
