package com.example.demo.reqres;

import feign.FeignException;
import feign.Request;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class ReqresFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            Request request = requestTemplate.request();
            System.out.println(request.httpMethod() + " " + request.url());

            requestTemplate.header("x-api-key", "reqres-free-v1");
        };
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
            @Value("${your-username}") String username,
            @Value("${your-password}") String password
    ) {
        return new BasicAuthRequestInterceptor(username, password);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            int httpStatus = response.status();
            Request request = response.request();
            FeignException feignException = FeignException.errorStatus(methodKey, response);

            if (httpStatus == 400) {
                return new IllegalArgumentException();
            } else {
                return feignException;
            }
        };
    }
}
