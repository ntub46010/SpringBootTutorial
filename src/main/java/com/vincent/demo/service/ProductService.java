package com.vincent.demo.service;

import com.vincent.demo.aop.ActionType;
import com.vincent.demo.aop.EntityType;
import com.vincent.demo.aop.SendEmail;
import com.vincent.demo.auth.UserIdentity;
import com.vincent.demo.converter.ProductConverter;
import com.vincent.demo.entity.product.Product;
import com.vincent.demo.entity.product.ProductRequest;
import com.vincent.demo.entity.product.ProductResponse;
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
    private UserIdentity userIdentity;

    public ProductService(ProductRepository repository, UserIdentity userIdentity) {
        this.repository = repository;
        this.userIdentity = userIdentity;
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

    @SendEmail(entity = EntityType.PRODUCT, action = ActionType.CREATE)
    public ProductResponse createProduct(ProductRequest request) {
        Product product = ProductConverter.toProduct(request);
        product.setCreator(userIdentity.getId());
        repository.insert(product);

        return ProductConverter.toProductResponse(product);
    }

    @SendEmail(entity = EntityType.PRODUCT, action = ActionType.UPDATE, idParamIndex = 0)
    public ProductResponse replaceProduct(String id, ProductRequest request) {
        Product oldProduct = getProduct(id);
        Product newProduct = ProductConverter.toProduct(request);
        newProduct.setId(oldProduct.getId());
        newProduct.setCreator(oldProduct.getCreator());

        repository.save(newProduct);

        return ProductConverter.toProductResponse(newProduct);
    }

    @SendEmail(entity = EntityType.PRODUCT, action = ActionType.DELETE, idParamIndex = 0)
    public void deleteProduct(String id) {
        repository.deleteById(id);
    }

    public List<ProductResponse> getProductResponses(ProductQueryParameter param) {
        String nameKeyword = Optional.ofNullable(param.getKeyword()).orElse("");
        int priceFrom = Optional.ofNullable(param.getPriceFrom()).orElse(0);
        int priceTo = Optional.ofNullable(param.getPriceTo()).orElse(Integer.MAX_VALUE);
        Sort sort = genSortingStrategy(param.getOrderBy(), param.getSortRule());

        List<Product> products = repository.findByPriceBetweenAndNameLikeIgnoreCase(priceFrom, priceTo, nameKeyword, sort);

        return products.stream()
                .map(ProductConverter::toProductResponse)
                .collect(Collectors.toList());
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
