package com.vincent.demo.controller;

import com.vincent.demo.entity.ProductRequest;
import com.vincent.demo.entity.ProductResponse;
import com.vincent.demo.parameter.QueryParameter;
import com.vincent.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") String id) {
        ProductResponse product = productService.getProductResponse(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.createProduct(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductResponse> replaceProduct(
            @PathVariable("id") String id, @Valid @RequestBody ProductRequest request) {
        ProductResponse product = productService.replaceProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(@ModelAttribute QueryParameter param) {
        List<ProductResponse> products = productService.getProductResponses(param);
        return ResponseEntity.ok(products);
    }

}
