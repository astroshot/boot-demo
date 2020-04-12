package com.boot.ut.config;

import com.boot.common.cache.service.CacheService;
import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;

public class RedisConfigTest extends AbstractTestCase {

    @Resource
    protected CacheService<String, String> cacheService;

    @Test
    public void testDefaultCacheService() {
        String key = "key-test";
        String val = "example";

        cacheService.put(key, val);
        String testVal = cacheService.get(key);
        Assert.assertEquals(val, testVal);
    }
}
