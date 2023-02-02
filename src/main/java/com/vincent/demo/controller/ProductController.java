package com.vincent.demo.controller;

import com.vincent.demo.exception.OperateAbsentItemsException;
import com.vincent.demo.model.DeleteByIdRequest;
import com.vincent.demo.model.Product;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.vincent.demo.util.DateUtil.toDate;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    private final Map<String, Product> productDB = new LinkedHashMap<>();

    @PostConstruct
    private void initData() {
        var products = List.of(
                new Product("B1", "Android (Java)", 380, toDate("2022-01-15")),
                new Product("B2", "Android (Kotlin)", 420, toDate("2022-05-15")),
                new Product("B3", "Data Structure (Java)", 250, toDate("2022-09-15")),
                new Product("B4", "Finance Management", 450, toDate("2022-07-15")),
                new Product("B5", "Human Resource Management", 330, toDate("2022-03-15"))
        );
        products.forEach(x -> productDB.put(x.getId(), x));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProducts(@RequestBody DeleteByIdRequest request) {
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
        var newCreatedFrom = Optional.ofNullable(createdFrom).orElse(new Date(0));
        var newCreatedTo = Optional.ofNullable(createdTo).orElse(new Date());
        var newName = Optional.ofNullable(name).orElse("");

        var products = productDB.values()
                .stream()
                .filter(p -> p.getCreatedTime().after(newCreatedFrom))
                .filter(p -> p.getCreatedTime().before(newCreatedTo))
                .filter(p -> p.getName().toLowerCase().contains(newName))
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }
}
