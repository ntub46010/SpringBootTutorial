package com.vincent.demo.converter;

import com.vincent.demo.entity.Product;
import com.vincent.demo.entity.ProductRequest;

public class ProductConverter {

    private ProductConverter() {

    }

    public static Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }
}
