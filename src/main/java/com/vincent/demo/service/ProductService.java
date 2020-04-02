package com.vincent.demo.service;

import com.vincent.demo.converter.ProductConverter;
import com.vincent.demo.entity.Product;
import com.vincent.demo.entity.ProductRequest;
import com.vincent.demo.exception.NotFoundException;
import com.vincent.demo.parameter.QueryParameter;
import com.vincent.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Product> getProducts(QueryParameter param) {
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

        if (sort != null) {
            return repository.findByNameLike(keyword, sort);
        }

        return repository.findByNameLike(keyword);
    }

}
