package com.boot.config;

import com.boot.common.cache.config.RedisConfig;
import com.boot.common.cache.service.CacheService;
import com.boot.common.cache.service.impl.LocalCacheServiceImpl;
import com.boot.common.cache.service.impl.StringRedisCacheServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.security.PublicKey;

@Configuration
public class CacheConfig {

    public static final String KEY_PREFIX = "cache.key.prefix";
    public static final String PREFIX_REDIS = "cache.redis";
    private static final String ACTIVE_KEY = "cache.active";
    private static final String REDIS = "redis";
    public static String PREFIX_KEY = "boot-demo-dao";

    @Resource
    protected Environment env;

    @Resource
    protected RedisConfig redisConfig;

    @Bean
    @ConfigurationProperties(prefix = PREFIX_REDIS)
    public RedisConfig getRedisConfig() {
        return new RedisConfig();
    }

    @Bean(name = "DefaultCacheService")
    public CacheService<String, String> createCacheService() {
        if (StringUtils.isNotBlank(env.getProperty(KEY_PREFIX))) {
            PREFIX_KEY = env.getProperty(KEY_PREFIX);
        }

        if (REDIS.equalsIgnoreCase(env.getProperty(ACTIVE_KEY))) {
            Assert.isTrue(redisConfig.getMaxTotal() > 0, "redis maxTotal should be positive");
            Assert.isTrue(redisConfig.getMaxIdle() > 0, "redis maxIdle should be positive");
            Assert.hasLength(redisConfig.getHost(), "redis host should not be empty");
            Assert.isTrue(redisConfig.getPort() > 0, "redis port should be positive");

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(redisConfig.getMaxTotal());
            config.setMaxIdle(redisConfig.getMaxIdle());
            JedisPool jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort());
            return new StringRedisCacheServiceImpl(jedisPool, PREFIX_KEY);
        } else {
            return new LocalCacheServiceImpl<>();
        }
    }

    @Bean(name = "RSACacheService")
    public CacheService<String, PublicKey> createRSACacheService() {
        return new LocalCacheServiceImpl<>();
    }

}
