package com.vincent.demo.service;

import com.vincent.demo.converter.ProductConverter;
import com.vincent.demo.entity.Product;
import com.vincent.demo.entity.ProductRequest;
import com.vincent.demo.entity.ProductResponse;
import com.vincent.demo.exception.NotFoundException;
import com.vincent.demo.parameter.QueryParameter;
import com.vincent.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

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

    public List<ProductResponse> getProductResponses(QueryParameter param) {
        String orderBy = param.getOrderBy();
        String sortRule = param.getSortRule();
        String keyword = param.getKeyword();

        Sort sort = null;
        if (orderBy != null && sortRule != null) {
            Sort.Direction direction = sortRule.equals("asc")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;

            sort = new Sort(direction, orderBy);
        }

        if (keyword == null) {
            keyword = "";
        }

        List<Product> products;
        if (sort != null) {
            products = repository.findByNameLike(keyword, sort);
        } else {
            products = repository.findByNameLike(keyword);
        }

        return products.stream()
                .map(ProductConverter::toProductResponse)
                .collect(Collectors.toList());
    }

}
