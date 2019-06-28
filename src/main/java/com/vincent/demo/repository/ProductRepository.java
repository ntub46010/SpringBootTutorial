package com.vincent.demo.repository;

import com.vincent.demo.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByNameLike(String productName);

    List<Product> findByNameLike(String productName, Sort sort);

}
