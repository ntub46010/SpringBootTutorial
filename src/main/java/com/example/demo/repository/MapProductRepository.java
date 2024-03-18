package com.example.demo.repository;

import com.example.demo.exception.UnprocessableEntityException;
import com.example.demo.model.ProductPO;
import com.example.demo.param.ProductRequestParameter;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

@Repository("mapProductRepository")
public class MapProductRepository implements IProductRepository {
    private static final Map<String, ProductPO> productMap = new HashMap<>();

    static {
        Stream.of(
                ProductPO.of("P1", "Android Development (Java)", 380, "U1"),
                ProductPO.of("P2", "Android Development (Kotlin)", 420, "U2"),
                ProductPO.of("P3", "Data Structure (Java)", 250, "U1"),
                ProductPO.of("P4", "Finance Management", 450, "U2"),
                ProductPO.of("P5", "Human Resource Management", 330, "U1")
        ).forEach(p -> productMap.put(p.getId(), p));
    }

    public ProductPO getOneById(String id) {
        return productMap.get(id);
    }

    public ProductPO insert(ProductPO product) {
        if (product.getId() == null) {
            product.setId(generateRandomId());
        }

        if (productMap.containsKey(product.getId())) {
            throw new UnprocessableEntityException("Product id " + product.getId() + " is existing.");
        }

        product.setCreatedTime(Instant.now().getEpochSecond());
        product.setUpdatedTime(product.getCreatedTime());
        productMap.put(product.getId(), product);

        return product;
    }

    public void update(ProductPO product) {
        if (!productMap.containsKey(product.getId())) {
            throw new UnprocessableEntityException("Product " + product.getId() + " doesn't exist.");
        }

        product.setUpdatedTime(Instant.now().getEpochSecond());
        productMap.put(product.getId(), product);
    }

    public void deleteById(String id) {
        productMap.remove(id);
    }

    public List<ProductPO> getMany(ProductRequestParameter param) {
        var sortField = param.getSortField();
        var sortDir = param.getSortDir();
        var keyword = param.getSearchKey();

        Comparator<ProductPO> comparator;
        if ("name".equals(sortField)) {
            comparator = Comparator.comparing(x -> x.getName().toLowerCase());
        } else if ("price".equals(sortField)) {
            comparator = Comparator.comparing(ProductPO::getPrice);
        } else {
            comparator = (a, b) -> 0;
        }

        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        return productMap.values()
                .stream()
                .filter(x -> x.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        x.getId().contains(keyword))
                .sorted(comparator)
                .toList();
    }

    private String generateRandomId() {
        var uuid = UUID.randomUUID().toString();
        return uuid.substring(0, uuid.indexOf("-"));
    }
}
