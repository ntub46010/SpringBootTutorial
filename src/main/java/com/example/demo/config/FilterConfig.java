package com.example.demo.config;

import com.example.demo.filter.LogApiFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LogApiFilter> logApiFilter() {
        var bean = new FilterRegistrationBean<LogApiFilter>();
        bean.setFilter(new LogApiFilter());
        bean.setUrlPatterns(List.of("/*"));
        bean.setOrder(0);

        return bean;
    }
}
