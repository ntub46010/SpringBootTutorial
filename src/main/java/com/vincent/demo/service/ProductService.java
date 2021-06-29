package com.vincent.demo.service;

import com.vincent.demo.converter.ProductConverter;
import com.vincent.demo.entity.Product;
import com.vincent.demo.entity.ProductRequest;
import com.vincent.demo.exception.NotFoundException;
import com.vincent.demo.parameter.ProductQueryParameter;
import com.vincent.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Product getProduct(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Product createProduct(ProductRequest request) {
        Product product = ProductConverter.toProduct(request);
        return repository.insert(product);
    }

    public Product replaceProduct(String id, ProductRequest request) {
        Product oldProduct = getProduct(id);
        Product newProduct = ProductConverter.toProduct(request);
        newProduct.setId(oldProduct.getId());

        return repository.save(newProduct);
    }

    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        String keyword = Optional.ofNullable(param.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(param.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(param.getPriceTo()).orElse(Integer.MAX_VALUE);

        Sort sort = genSortingStrategy(param.getOrderBy(), param.getSortRule());

        return repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, keyword, sort);
    }

    private Sort genSortingStrategy(String orderBy, String sortRule) {
        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = Sort.by(direction, orderBy);
        }

        return sort;
    }

}
