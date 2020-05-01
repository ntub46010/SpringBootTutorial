package com.vincent.demo.converter;

import com.vincent.demo.entity.Product;
import com.vincent.demo.entity.ProductResponse;
import com.vincent.demo.entity.ProductRequest;

public class ProductConverter {

    private ProductConverter() {

    }

    public static ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());

        return response;
    }

    public static Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return product;
    }
}
