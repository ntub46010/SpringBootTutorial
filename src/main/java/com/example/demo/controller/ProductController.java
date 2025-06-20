package com.example.demo.controller;

import com.example.demo.model.ProductRequest;
import com.example.demo.model.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") String id) {
        var product = createTestProduct(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getProducts(
            @RequestParam(name = "sortField", required = false) String sortField,
            @RequestParam(name = "sortDirection", required = false) String sortDirection) {

        var p1 = createTestProduct("1");
        var p2 = createTestProduct("2");
        var products = List.of(p1, p2);

        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productReq) {
        if (productReq.getPrice() < 0) {
            return ResponseEntity.unprocessableEntity().build();
        }

        var product = new ProductResponse();
        product.setId("testId");
        product.setName(productReq.getName());
        product.setPrice(productReq.getPrice());

        return ResponseEntity.ok(product);
    }

    private ProductResponse createTestProduct(String id) {
        var product = new ProductResponse();
        product.setId(id);
        product.setName("Test " + id);
        product.setPrice(100);

        return product;
    }
}
