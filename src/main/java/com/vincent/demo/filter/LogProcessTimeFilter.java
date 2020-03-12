package com.vincent.demo.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogProcessTimeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        long processTime = System.currentTimeMillis() - startTime;

        System.out.println(processTime + " ms");
    }
}
