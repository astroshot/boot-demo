package com.boot.common.cache.service.impl;

import com.boot.common.cache.service.CacheService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LocalCacheServiceImpl<K, V> implements CacheService<K, V> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private Cache<K, V> cache = CacheBuilder.newBuilder().build();

    @Override
    public boolean put(K k, V v) {
        try {
            cache.put(k, v);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean put(K k, V v, int expireTime) {
        return put(k, v);
    }

    @Override
    public V get(K k) {
        try {
            return cache.getIfPresent(k);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public V remove(K k) {
        V val = get(k);
        cache.invalidate(k);
        return val;
    }
}
