package com.vincent.demo.converter;

import com.vincent.demo.entity.product.Product;
import com.vincent.demo.entity.product.ProductResponse;
import com.vincent.demo.entity.product.ProductRequest;

public class ProductConverter {

    private ProductConverter() {

    }

    public static ProductResponse toProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setCreatorId(product.getCreator());

        return response;
    }

    public static Product toProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());

        if (request.isSoftDelete()) {
            product.setPrice(Integer.MIN_VALUE);
        } else {
            product.setPrice(request.getPrice());
        }

        return product;
    }
}
