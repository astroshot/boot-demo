package com.boot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * 自定义Tomcat容器配置类
 */
@Component
public class BootTomcatWebServerFactoryCustomizer
        implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    public static final int DEFAULT_MAX_PARAMETER_COUNT = -1;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 单次请求参数最大限制数
     */
    @Value("${server.tomcat.maxParameterCount}")
    private int maxParameterCount = DEFAULT_MAX_PARAMETER_COUNT;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        if (logger.isDebugEnabled()) {
            logger.debug("MyTomcatWebServerFactoryCustomizer customize");
        }

        PropertyMapper propertyMapper = PropertyMapper.get();

        propertyMapper.from(this::getMaxParameterCount)
                .when((maxParameterCount) -> maxParameterCount != DEFAULT_MAX_PARAMETER_COUNT)
                .to((maxParameterCount) -> customizerMaxParameterCount(factory, maxParameterCount));
    }

    /**
     * 配置内置Tomcat单次请求参数限制
     *
     * @param factory
     * @param maxParameterCount
     */
    private void customizerMaxParameterCount(TomcatServletWebServerFactory factory,
                                             int maxParameterCount) {
        factory.addConnectorCustomizers(
                connector -> connector.setMaxParameterCount(maxParameterCount));
    }

    public void setMaxParameterCount(int maxParameterCount) {
        this.maxParameterCount = maxParameterCount;
    }

    public int getMaxParameterCount() {
        return maxParameterCount;
    }
}
