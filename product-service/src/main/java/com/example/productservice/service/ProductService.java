package com.example.productservice.service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    void createProduct(ProductRequest productRequest);

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    void deleteProduct(Long id);

    List<ProductResponse> getAllProducts();
}
