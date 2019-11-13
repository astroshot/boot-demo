package com.boot.common.web.filter;

import com.boot.common.web.filter.wrapper.XSSHTTPServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class XSSHTTPServletFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            XSSHTTPServletRequestWrapper xssRequest = new XSSHTTPServletRequestWrapper(request);
            chain.doFilter(xssRequest, response);
        } catch (Exception e) {
            logger.error("Xss过滤器，包装request对象失败", e);
            chain.doFilter(request, response);
        }
    }
}
