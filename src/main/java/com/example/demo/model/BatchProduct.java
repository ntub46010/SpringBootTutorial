package com.example.demo.model;

import jakarta.validation.Valid;

import java.util.List;

public class BatchProduct {
    private List<@Valid Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
