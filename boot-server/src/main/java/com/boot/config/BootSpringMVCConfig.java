package com.boot.config;

import com.boot.common.web.config.SpringMVCConfig;
import com.boot.common.web.filter.LogFilter;
import com.boot.common.web.model.MailInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BootSpringMVCConfig extends SpringMVCConfig {

    @Bean
    public FilterRegistrationBean<LogFilter> logFilterLogInfoFilterBean() {
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LogFilter());
        registrationBean.setOrder(4);
        registrationBean.addUrlPatterns("/**");
        return registrationBean;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.mail")
    public MailInfo getMailInfo() {
        return new MailInfo();
    }
}
