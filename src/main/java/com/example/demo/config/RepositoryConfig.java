package com.example.demo.config;

import com.example.demo.repository.IProductRepository;
import com.example.demo.repository.ListProductRepository;
import com.example.demo.repository.MapProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public IProductRepository productRepository(
            @Value("${product-repository.storage}") String storageType
    ) {
        if ("map".equalsIgnoreCase(storageType)) {
            System.out.println("Create MapProductRepository.");
            return new MapProductRepository();
        } else if ("list".equalsIgnoreCase(storageType)) {
            System.out.println("Create ListProductRepository.");
            return new ListProductRepository();
        } else {
            throw new IllegalArgumentException("Provided product repository storage type is unsupported.");
        }
    }
}
