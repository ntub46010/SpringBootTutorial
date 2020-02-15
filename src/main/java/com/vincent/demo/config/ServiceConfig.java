package com.vincent.demo.config;

import com.vincent.demo.repository.ProductRepository;
import com.vincent.demo.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ProductService productService(ProductRepository repository) {
        return new ProductService(repository);
    }

}
