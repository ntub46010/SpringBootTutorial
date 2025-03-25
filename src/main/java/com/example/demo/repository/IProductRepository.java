package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.param.ProductRequestParameter;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public interface IProductRepository {
    Product findById(String id);
    List<Product> findAll(ProductRequestParameter param);
    Product insert(Product product);
    void deleteAll();

    default String generateId() {
        return UUID.randomUUID().toString();
    }

    default Comparator<Product> getSortComparator(ProductRequestParameter param) {
        Comparator<Product> comparator;
        if ("name".equalsIgnoreCase(param.getSortField())) {
            comparator = Comparator.comparing(p -> p.getName().toLowerCase());
        } else if ("price".equalsIgnoreCase(param.getSortField())) {
            comparator = Comparator.comparing(Product::getPrice);
        } else {
            comparator = (p1, p2) -> 0;
        }

        if ("desc".equalsIgnoreCase(param.getSortDirection())) {
            comparator = comparator.reversed();
        }

        return comparator;
    }
}
