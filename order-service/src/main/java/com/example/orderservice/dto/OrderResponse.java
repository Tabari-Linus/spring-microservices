package com.example.orderservice.dto;

import com.example.orderservice.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long productId;
    private int quantity;
    private double totalPrice;
    private OrderStatus status;
}
