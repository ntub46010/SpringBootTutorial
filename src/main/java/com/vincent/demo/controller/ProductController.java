package com.vincent.demo.controller;

import com.vincent.demo.entity.Product;
import com.vincent.demo.parameter.QueryParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private List<Product> productDB = new ArrayList<>();

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) {
        Optional<Product> productOp = productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (productOp.isPresent()) {
            Product product = productOp.get();
            return ResponseEntity.ok().body(product);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product request) {
        boolean isIdDuplicated = productDB.stream()
                .anyMatch(p -> p.getId().equals(request.getId()));

        if (isIdDuplicated) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.add(product);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> replaceProduct(
            @PathVariable("id") String id, @RequestBody Product request) {
        Optional<Product> productOp = productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (!productOp.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Product oldProduct = productOp.get();
        int productIndex = productDB.indexOf(oldProduct);

        Product product = new Product();
        product.setId(oldProduct.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.set(productIndex, product);

        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") String id) {
        Optional<Product> productOp = productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (productOp.isPresent()) {
            Product product = productOp.get();
            productDB.remove(product);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@ModelAttribute QueryParameter param) {
        Stream<Product> stream = productDB.stream();

        if (param.getKeyword() != null) {
            stream = stream
                    .filter(p -> p.getName().contains(param.getKeyword()));
        }

        if ("price".equals(param.getOrderBy()) && param.getSortRule() != null) {
            Comparator<Product> comparator = param.getSortRule().equals("asc")
                    ? Comparator.comparing(Product::getPrice)
                    : Comparator.comparing(Product::getPrice).reversed();

            stream = stream.sorted(comparator);
        }

        List<Product> products = stream.collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }
}
