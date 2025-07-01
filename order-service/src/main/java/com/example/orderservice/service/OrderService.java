package com.example.orderservice.service;

import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.model.Order;

import java.util.List;

public interface OrderService {

    void createOrder(Order order);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
}
