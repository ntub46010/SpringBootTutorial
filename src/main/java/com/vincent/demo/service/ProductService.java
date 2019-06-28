package com.vincent.demo.service;

import com.vincent.demo.entity.Product;
import com.vincent.demo.exception.NotFoundException;
import com.vincent.demo.exception.UnprocessableException;
import com.vincent.demo.parameter.QueryParameter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductService {

    private List<Product> productDB = new ArrayList<>();

    public Product getProduct(String id) {
        return productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Product createProduct(Product request) {
        boolean isIdDuplicated = productDB.stream()
                .anyMatch(p -> p.getId().equals(request.getId()));
        if (isIdDuplicated) {
            throw new UnprocessableException("Id is duplicated.");
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.add(product);

        return product;
    }

    public Product replaceProduct(String id, Product request) {
        Product oldProduct = getProduct(id);
        int productIndex = productDB.indexOf(oldProduct);

        Product product = new Product();
        product.setId(oldProduct.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.set(productIndex, product);

        return product;
    }

    public void deleteProduct(String id) {
        Optional<Product> productOp = productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (productOp.isPresent()) {
            Product product = productOp.get();
            productDB.remove(product);
        }
    }
    public List<Product> getProducts(QueryParameter param) {
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

        return stream.collect(Collectors.toList());
    }

}
