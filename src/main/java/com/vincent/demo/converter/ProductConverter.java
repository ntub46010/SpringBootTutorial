package com.vincent.demo.converter;

import com.vincent.demo.entity.Product;
import com.vincent.demo.entity.ProductResponse;

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
}
