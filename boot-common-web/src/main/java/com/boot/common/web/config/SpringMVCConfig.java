package com.boot.common.web.config;

import com.boot.common.web.filter.XSSHTTPServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
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

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }

}
