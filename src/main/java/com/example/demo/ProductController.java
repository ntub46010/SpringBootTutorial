package com.example.demo;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
    private static final Map<String, Product> productMap = new HashMap<>();

    static {
        var p1 = Product.of("101", "Coke", 30);
        var p2 = Product.of("102", "Hamburger", 60);
        var p3 = Product.of("103", "Sandwich", 50);
        Stream.of(p1, p2, p3).forEach(p -> productMap.put(p.getId(), p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
        var product =  productMap.get(id);
        return product == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(name = "sortField", required = false) String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "ASC") SortDirection sortDirection) {
        Comparator<Product> comparator = (p1, p2) -> 0;
        if ("name".equalsIgnoreCase(sortField)) {
            comparator = Comparator.comparing(p -> p.getName().toLowerCase());
        } else if ("price".equalsIgnoreCase(sortField)) {
            comparator = Comparator.comparing(Product::getPrice);
        }

        if (sortDirection == SortDirection.DESC) {
            comparator = comparator.reversed();
        }

        var products = productMap.values().stream()
                .sorted(comparator)
                .toList();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        if (StringUtils.hasText(product.getId())) {
            return ResponseEntity.badRequest().build();
        }

        var isIdExisting = productMap.containsKey(product.getId());
        if (isIdExisting) {
            return ResponseEntity.unprocessableEntity().build();
        }

        productMap.put(product.getId(), product);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable("id") String id, @RequestBody Product product) {
        var isIdExisting = productMap.containsKey(id);
        if (!isIdExisting) {
            return ResponseEntity.notFound().build();
        }

        product.setId(id);
        productMap.put(product.getId(), product);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        productMap.remove(id);
        return ResponseEntity.ok().build();
    }
}
