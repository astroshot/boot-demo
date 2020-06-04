package com.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class BootWebSocketConfig {

    @Bean
    public ServerEndpointExporter newServerEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
