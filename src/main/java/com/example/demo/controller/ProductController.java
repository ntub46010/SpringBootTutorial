package com.example.demo.controller;

import com.example.demo.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class ProductController {
    private static final Map<String, Product> productMap = new HashMap<>();

    static {
        Stream.of(
                Product.of("B1", "Android Development (Java)", 380),
                Product.of("B2", "Android Development (Kotlin)", 420),
                Product.of("B3", "Data Structure (Java)", 250),
                Product.of("B4", "Finance Management", 450),
                Product.of("B5", "Human Resource Management", 330)
        ).forEach(p -> productMap.put(p.getId(), p));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String productId) {
        var product = productMap.get(productId);
        return product == null
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PostMapping("/products")
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        if (product.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (productMap.containsKey(product.getId())) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        productMap.put(product.getId(), product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable("id") String productId, @RequestBody Product request) {
        var product = productMap.get(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String productId) {
        if (!productMap.containsKey(productId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        productMap.remove(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}