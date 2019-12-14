package com.boot.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService getThreadPool(){
        return Executors.newScheduledThreadPool(5);
    }

}
