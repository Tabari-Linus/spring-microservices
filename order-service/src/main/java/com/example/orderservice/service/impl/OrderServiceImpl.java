package com.example.orderservice.service.impl;

import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.exception.OrderNotFoundException;
import com.example.orderservice.exception.ProductNotFoundException;
import com.example.orderservice.model.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;


    @Override
    @CircuitBreaker(name = "productServiceBreaker", fallbackMethod = "createOrderFallback")
    public void createOrder(Order order) {
        String productUrl = "http://product-service/api/v1/products/"+ order.getProductId();
        ProductDto productDto;
        try {
            productDto = restTemplate.getForObject(productUrl, ProductDto.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ProductNotFoundException(order.getProductId());
        } catch (Exception ex) {
            throw new RuntimeException("Error calling Product Service: " + ex.getMessage());
        }

        if (productDto == null || productDto.getId() == null) {
            throw new RuntimeException("Product not found or invalid response from Product Service");
        }

        if (productDto.getStockQuantity() < order.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for " + productDto.getName() + ". Available stock: " + productDto.getStockQuantity());
        }

        order.setTotalPrice(order.getQuantity() * productDto.getPrice());
        order.setStatus(OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = mapToOrderResponse(order);
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return mapToOrderResponse(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setProductId(order.getProductId());
        orderResponse.setQuantity(order.getQuantity());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setStatus(order.getStatus());
        return orderResponse;
    }

    public ResponseEntity<String> createOrderFallback(Order order, Throwable t) {
        System.err.println("Fallback activated for createOrder due to: " + t.getMessage());

        if (t instanceof HttpClientErrorException.NotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(null);
    }
}
