package com.example.demo.repository;

import com.example.demo.exception.UnprocessableEntityException;
import com.example.demo.model.ProductPO;
import com.example.demo.param.ProductRequestParameter;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Repository("listProductRepository")
public class ListProductRepository implements IProductRepository {
    private static final List<ProductPO> productList = new ArrayList<>();

    static {
        Stream.of(
                ProductPO.of("P1", "Android Development (Java)", 380, "U1"),
                ProductPO.of("P2", "Android Development (Kotlin)", 420, "U2"),
                ProductPO.of("P3", "Data Structure (Java)", 250, "U1"),
                ProductPO.of("P4", "Finance Management", 450, "U2"),
                ProductPO.of("P5", "Human Resource Management", 330, "U1")
        ).forEach(productList::add);
    }

    public ProductPO getOneById(String id) {
        return productList
                .stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public ProductPO insert(ProductPO product) {
        if (product.getId() == null) {
            product.setId(generateRandomId());
        }

        if (getOneById(product.getId()) != null) {
            throw new UnprocessableEntityException("Product id " + product.getId() + " is existing.");
        }

        product.setCreatedTime(Instant.now().getEpochSecond());
        product.setUpdatedTime(product.getCreatedTime());
        productList.add(product);

        return product;
    }

    public void update(ProductPO product) {
        var index = -1;
        for (var i = 0; i < productList.size(); i++) {
            if (productList.get(i).getId().equals(product.getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new UnprocessableEntityException("Product " + product.getId() + " doesn't exist.");
        }

        product.setUpdatedTime(Instant.now().getEpochSecond());
        productList.set(index, product);
    }

    public void deleteById(String id) {
        productList.removeIf(p -> p.getId().equals(id));
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

        return productList
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
