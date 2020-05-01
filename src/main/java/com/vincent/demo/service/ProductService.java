package com.vincent.demo.service;

import com.vincent.demo.converter.ProductConverter;
import com.vincent.demo.entity.Product;
import com.vincent.demo.entity.ProductRequest;
import com.vincent.demo.entity.ProductResponse;
import com.vincent.demo.exception.NotFoundException;
import com.vincent.demo.parameter.ProductQueryParameter;
import com.vincent.demo.repository.ProductRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Optional;

public class ProductService {

    private ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product getProduct(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public ProductResponse getProductResponse(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
        return ProductConverter.toProductResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product = repository.insert(product);

        return ProductConverter.toProductResponse(product);
    }

    public ProductResponse replaceProduct(String id, ProductRequest request) {
        Product oldProduct = getProduct(id);
        Product newProduct = ProductConverter.toProduct(request);
        newProduct.setId(oldProduct.getId());

        repository.save(newProduct);

        return ProductConverter.toProductResponse(newProduct);
    }

    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    public List<ProductResponse> getProductResponses(ProductQueryParameter param) {
        String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(param.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(param.getPriceTo()).orElse(Integer.MAX_VALUE);
        Sort sort = configureSort(param.getOrderBy(), param.getSortRule());

        List<Product> products = repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, nameKeyword, sort);

        return products.stream()
                .map(ProductConverter::toProductResponse)
                .collect(Collectors.toList());
    }

    private Sort configureSort(String orderBy, String sortRule) {
        Sort sort = Sort.unsorted();
        if (Objects.nonNull(orderBy) && Objects.nonNull(sortRule)) {
            Sort.Direction direction = Sort.Direction.fromString(sortRule);
            sort = new Sort(direction, orderBy);
        }

        return sort;
    }

}
