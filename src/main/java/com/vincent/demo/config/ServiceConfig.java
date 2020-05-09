package com.vincent.demo.config;

import com.vincent.demo.repository.ProductRepository;
import com.vincent.demo.service.MailService;
import com.vincent.demo.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public ProductService productService(ProductRepository repository,
                                         MailService mailService) {
        System.out.println("Create product service.");
        return new ProductService(repository, mailService);
    }

}
