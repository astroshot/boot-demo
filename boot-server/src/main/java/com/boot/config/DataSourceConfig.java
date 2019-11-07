package com.boot.config;

import com.boot.common.dao.interceptor.MybatisShowSqlInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Resource
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory createSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // located in folder boot-dao src/main/resources
        bean.setMapperLocations(resolver.getResources("classpath*:mappers/**Mapper.xml"));
        bean.setPlugins(new Interceptor[]{
                new MybatisShowSqlInterceptor()
        });
        return bean.getObject();
    }
}
