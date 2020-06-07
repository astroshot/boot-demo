package com.boot.common.web.config;

import brave.Tracing;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.ThreadLocalCurrentTraceContext;
import brave.servlet.TracingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import zipkin2.Span;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Reporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.okhttp3.OkHttpSender;

import javax.servlet.Filter;

@Configuration
public class TracingConfig {

    /**
     * zipkin addr
     */
    @Value("${zipkin.tracing.endpoint:http://localhost:8989/zipkin}")
    private String zipkinEndPoint;

    @Value("${zipkin.tracing.local-service-name:1.1-test}")
    private String localServiceName;

    /**
     * sender configuration
     */
    @Bean
    public Sender sender() {
        OkHttpSender sender = OkHttpSender
                .newBuilder()
                .endpoint(zipkinEndPoint)
                .build();
        return sender;
    }


    /**
     * reporter configuration
     */
    @Bean
    public Reporter<Span> reporter(Sender sender) {
        return AsyncReporter
                .builder(sender)
                .build();
    }

    /**
     * http tracing configuration
     */
    @Bean
    public Tracing tracing2(Reporter reporter) {
        return Tracing.newBuilder()
                .currentTraceContext(
                        ThreadLocalCurrentTraceContext.newBuilder()
                                .addScopeDecorator(MDCScopeDecorator.create())
                                .build())
                .localServiceName(localServiceName + "_http")
                // .sampler(CountingSampler.create(SAMPLE_RATE))
                // 发送到 zipkin server
                // .spanReporter(reporter)
                .build();
    }

    /**
     * servlet filter configuration
     */
    @Bean
    public Filter filter(Tracing tracing2) {
        return TracingFilter.create(tracing2);
    }


    /**
     * filter registration
     */
    @Bean
    public FilterRegistrationBean<Filter> filterRegistration(Filter filter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("zipkin-filter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
