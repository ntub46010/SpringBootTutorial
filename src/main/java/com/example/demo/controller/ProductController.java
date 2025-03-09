package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.param.ProductRequestParameter;
import com.example.demo.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    @Autowired
    private IProductRepository productRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getOne(@PathVariable("id") String id) {
        var product = productRepository.findById(id);
        return product == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getMany(@ModelAttribute ProductRequestParameter param) {
        var products = productRepository.findAll(param);
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Product product) {
        if (product.getName() == null || product.getPrice() < 0) {
            return ResponseEntity.unprocessableEntity().build();
        }

        productRepository.insert(product);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .build(Map.of("id", product.getId()));
        return ResponseEntity.created(uri).build();
    }
}
