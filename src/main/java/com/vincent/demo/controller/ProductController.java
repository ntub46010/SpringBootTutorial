package com.vincent.demo.controller;

import com.vincent.demo.exception.OperateAbsentItemsException;
import com.vincent.demo.model.BatchDeleteRequest;
import com.vincent.demo.model.Product;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    private final Map<String, Product> productDB = new LinkedHashMap<>();

    @PostConstruct
    private void initData() {
        var products = List.of(
                Product.of("B1", "Android (Java)", "2022-01-15"),
                Product.of("B2", "Android (Kotlin)", "2022-05-15"),
                Product.of("B3", "Data Structure (Java)", "2022-09-15"),
                Product.of("B4", "Finance Management", "2022-07-15"),
                Product.of("B5", "Human Resource Management", "2022-03-15")
        );
        products.forEach(x -> productDB.put(x.getId(), x));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProducts(@RequestBody BatchDeleteRequest request) {
        var itemIds = request.getIds();
        var absentIds = itemIds.stream()
                .filter(Predicate.not(productDB::containsKey))
                .collect(Collectors.toList());
        if (!absentIds.isEmpty()) {
            throw new OperateAbsentItemsException(absentIds);
        }

        itemIds.forEach(productDB::remove);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) Date createdFrom,
            @RequestParam(required = false) Date createdTo,
            @RequestParam(required = false) String name) {
        var stream = productDB.values().stream();
        if (createdFrom != null) {
            stream = stream.filter(p -> p.getCreatedTime().after(createdFrom));
        }
        if (createdTo != null) {
            stream = stream.filter(p -> p.getCreatedTime().before(createdTo));
        }
        if (name != null) {
            stream = stream.filter(p -> p.getName().toLowerCase().contains(name));
        }

        var products = stream.collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }
}
