package com.example.productservice.listener;

import com.example.productservice.event.OrderCreatedEvent;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class OrderCreatedEventListener {

    private final ProductRepository productRepository;

    public OrderCreatedEventListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @KafkaListener(topics = "order-created-topic", groupId = "product-service-group")
    @Transactional
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        System.out.println("Received OrderCreatedEvent: " + event);

        Optional<Product> productOptional = productRepository.findById(event.getProductId());

        if (productOptional.isEmpty()) {
            System.err.println("Product not found for ID: " + event.getProductId() + ". Cannot decrement stock.");
            return;
        }

        Product product = productOptional.get();

        if (product.getStockQuantity() < event.getQuantity()) {
            System.err.println("Insufficient stock for Product ID: " + event.getProductId() + ". Available: " + product.getStockQuantity() + ", Ordered: " + event.getQuantity() + ". Cannot decrement stock.");
            return;
        }

        product.setStockQuantity(product.getStockQuantity() - event.getQuantity());
        productRepository.save(product);

        System.out.println("Stock updated for Product ID: " + product.getId() + ". New stock: " + product.getStockQuantity());

    }
}