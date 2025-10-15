package com.example.demo.reqres;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GlobalFeignConfig {

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient.Builder()
                .callTimeout(20, TimeUnit.SECONDS)
                .build();
    }
}
