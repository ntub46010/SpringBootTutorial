package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.param.ProductRequestParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/products")
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

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String productId) {
        var product = productMap.get(productId);
        return product == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        if (product.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (productMap.containsKey(product.getId())) {
            return ResponseEntity.unprocessableEntity().build();
        }

        productMap.put(product.getId(), product);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .build(Map.of("id", product.getId()));

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable("id") String productId, @RequestBody Product request) {
        var product = productMap.get(productId);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String productId) {
        if (!productMap.containsKey(productId)) {
            return ResponseEntity.notFound().build();
        }

        productMap.remove(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @ModelAttribute ProductRequestParameter param
    ) {
        var orderField = param.getOrderField();
        var sortDir = param.getSortDir();
        var keyword = param.getSearchKey();

        Comparator<Product> comparator;
        if ("name".equals(orderField)) {
            comparator = Comparator.comparing(x -> x.getName().toLowerCase());
        } else if ("price".equals(orderField)) {
            comparator = Comparator.comparing(Product::getPrice);
        } else {
            comparator = (a, b) -> 0;
        }

        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        var products = productMap.values()
                .stream()
                .filter(x -> x.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        x.getId().contains(keyword))
                .sorted(comparator)
                .toList();
        return ResponseEntity.ok(products);
    }
}