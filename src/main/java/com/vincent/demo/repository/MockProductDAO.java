package com.vincent.demo.repository;

import com.vincent.demo.entity.Product;
import com.vincent.demo.parameter.ProductQueryParameter;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class MockProductDAO {

    private final List<Product> productDB = new ArrayList<>();

    public Product insert(Product product) {
        productDB.add(product);
        return product;
    }

    public Product replace(String id, Product product) {
        Optional<Product> productOp = find(id);
        productOp.ifPresent(p -> {
            p.setName(product.getName());
            p.setPrice(product.getPrice());
        });

        return product;
    }

    public void delete(String id) {
        productDB.removeIf(p -> p.getId().equals(id));
    }

    public Optional<Product> find(String id) {
        return productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public List<Product> find(ProductQueryParameter param) {
        String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();

        Comparator<Product> comparator = Objects.nonNull(orderBy) && Objects.nonNull(sortRule)
                ? configureSortComparator(orderBy, sortRule)
                : (p1, p2) -> 0;

        return productDB.stream()
                .filter(p -> p.getName().contains(nameKeyword))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private Comparator<Product> configureSortComparator(String orderBy, String sortRule) {
        Comparator<Product> comparator = (p1, p2) -> 0;

        if (orderBy.equalsIgnoreCase("price")) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if (orderBy.equalsIgnoreCase("name")) {
            comparator = Comparator.comparing(Product::getName);
        }

        if (sortRule.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }

        return comparator;
    }

}
