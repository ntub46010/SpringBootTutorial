package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.param.ProductRequestParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapProductRepository implements IProductRepository {
    private final Map<String, Product> productMap = new HashMap<>();

    public Product findById(String id) {
        return productMap.get(id);
    }

    public List<Product> findAll(ProductRequestParameter param) {
        var comparator = getSortComparator(param);
        return productMap.values()
                .stream()
                .sorted(comparator)
                .toList();
    }

    public Product insert(Product product) {
        product.setId(generateId());
        productMap.put(product.getId(), product);

        return product;
    }

    public void deleteAll() {
        productMap.clear();
    }
}
