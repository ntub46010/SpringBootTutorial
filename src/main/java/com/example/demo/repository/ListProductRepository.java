package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.param.ProductRequestParameter;

import java.util.ArrayList;
import java.util.List;

public class ListProductRepository implements IProductRepository {
    private final List<Product> productList = new ArrayList<>();

    public Product findById(String id) {
        return productList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Product> findAll(ProductRequestParameter param) {
        var comparator = getSortComparator(param);
        return productList.stream()
                .sorted(comparator)
                .toList();
    }

    public Product insert(Product product) {
        product.setId(generateId());
        productList.add(product);

        return product;
    }

    public void deleteAll() {
        productList.clear();
    }
}
