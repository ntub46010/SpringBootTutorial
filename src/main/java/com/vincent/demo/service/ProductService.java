package com.vincent.demo.service;

import com.vincent.demo.entity.Product;
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

    public Product createProduct(Product request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.insert(product);
    }

    public Product replaceProduct(String id, Product request) {
        Product oldProduct = getProduct(id);

        Product product = new Product();
        product.setId(oldProduct.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.save(product);
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
