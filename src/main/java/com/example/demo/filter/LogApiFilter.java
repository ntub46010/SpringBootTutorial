package com.example.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

public class LogApiFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        var requestWrapper = new ContentCachingRequestWrapper(request);
        var responseWrapper = new ContentCachingResponseWrapper(response);

        var startTime = System.currentTimeMillis();
        filterChain.doFilter(requestWrapper, responseWrapper);
        var endTime = System.currentTimeMillis();

        var sb = new StringBuilder();
        sb.append(request.getMethod()).append(" ").append(request.getRequestURI());

        var queryString = request.getQueryString();
        if (StringUtils.hasText(queryString)) {
            sb.append("?").append(queryString);
        }

        sb.append("\n").append("Request Content Type: ").append(request.getHeader(HttpHeaders.CONTENT_TYPE));
        sb.append("\n").append("Process Time: ").append(endTime - startTime).append(" ms");
        sb.append("\n").append("Status Code: ").append(response.getStatus());

        var requestBodyStr = getBodyString(requestWrapper.getContentAsByteArray());
        if (StringUtils.hasText(requestBodyStr)) {
            sb.append("\n").append("Request Body: ").append(requestBodyStr);
        }

        var responseBodyStr = getBodyString(responseWrapper.getContentAsByteArray());
        if (StringUtils.hasText(responseBodyStr)) {
            sb.append("\n").append("Response Body: ").append(responseBodyStr);
        }

        sb.append("\n").append("-");

        System.out.println(sb);
        responseWrapper.copyBodyToResponse();
    }

    private String getBodyString(byte[] content) {
        String body = new String(content);
        return body.replaceAll("[\n\r]", "");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        var uri = request.getRequestURI();
        return StringUtils.startsWithIgnoreCase(uri, "/articles");
    }
}








