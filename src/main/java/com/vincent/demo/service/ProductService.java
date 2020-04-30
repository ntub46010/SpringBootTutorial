package com.vincent.demo.service;

import com.vincent.demo.entity.Product;
import com.vincent.demo.exception.NotFoundException;
import com.vincent.demo.exception.UnprocessableException;
import com.vincent.demo.parameter.ProductQueryParameter;
import com.vincent.demo.repository.MockProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private MockProductDAO productDAO;

    public Product createProduct(Product request) {
        boolean isIdDuplicated = productDAO.find(request.getId()).isPresent();
        if (isIdDuplicated) {
            throw new UnprocessableException("Id is duplicated.");
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return productDAO.insert(product);
    }

    public Product getProduct(String id) {
        return productDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Product replaceProduct(String id, Product request) {
        Product product = getProduct(id);
        return productDAO.replace(product.getId(), request);
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productDAO.delete(product.getId());
    }

    public List<Product> getProducts(ProductQueryParameter param) {
        return productDAO.find(param);
    }

}
