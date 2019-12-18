package com.boot.common.cache.service.impl;

import com.boot.common.cache.AbstractRedisCacheService;
import redis.clients.jedis.JedisPool;


public class StringRedisCacheServiceImpl extends AbstractRedisCacheService<String, String> {

    public StringRedisCacheServiceImpl(JedisPool jedisPool, String prefix) {
        super(jedisPool, prefix);
    }
}
