package com.example.demo.repository;

import com.example.demo.model.ProductPO;
import com.example.demo.param.ProductRequestParameter;

import java.util.List;

public interface IProductRepository {
    ProductPO getOneById(String id);
    ProductPO insert(ProductPO product);
    void update(ProductPO product);
    void deleteById(String id);
    List<ProductPO> getMany(ProductRequestParameter param);
}
