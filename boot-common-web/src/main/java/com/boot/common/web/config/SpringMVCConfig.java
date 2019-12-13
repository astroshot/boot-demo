package com.boot.common.web.config;

import com.boot.common.web.filter.XSSHTTPServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


public class SpringMVCConfig extends WebMvcConfigurationSupport {

    @Bean
    public FilterRegistrationBean<XSSHTTPServletFilter> createXSSHTTPServletFilterRegistrationBean() {
        // 新建过滤器注册类
        FilterRegistrationBean<XSSHTTPServletFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XSSHTTPServletFilter());
        registration.setOrder(1);
        // 设置过滤器的URL模式
        registration.addUrlPatterns("/**");
        return registration;
    }
}
