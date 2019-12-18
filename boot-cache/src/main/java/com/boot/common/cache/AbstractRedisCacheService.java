package com.boot.common.cache;

import com.boot.common.cache.service.CacheService;
import com.boot.common.helper.JSONHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class AbstractRedisCacheService<K, V> implements CacheService<K, V> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private JedisPool jedisPool;

    private Type valueType;

    private String prefix;

    public AbstractRedisCacheService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        valueType = pt.getActualTypeArguments()[1];
    }

    public AbstractRedisCacheService(JedisPool jedisPool, String prefix) {
        this();
        this.jedisPool = jedisPool;
        this.prefix = prefix;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String getActualKey(K k) {
        if (k == null) {
            throw new NullPointerException("parameter k cannot be null");
        }

        if (k instanceof String) {
            return String.format("%s-%s", prefix, k);
        }

        return String.format("%s-%s", prefix, JSONHelper.toJSONString(k));
    }

    @Override
    public boolean put(K k, V v, int expireTime) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = getActualKey(k);
            String val = JSONHelper.toJSONString(v);

            if (expireTime > 0) {
                jedis.setex(key, expireTime, val);
                logger.info("put key: {}, val: {}, expireTime: {}", key, val, expireTime);
            } else {
                jedis.set(key, val);
                logger.info("put key: {}, val: {}", key, val);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null && jedis.isConnected()) {
                jedis.close();
            }
        }
        return false;
    }

    @Override
    public boolean put(K k, V v) {
        return put(k, v, 0);
    }

    @Override
    public V get(K k) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = getActualKey(k);
            String val = jedis.get(key);
            logger.info("get key: {}, val: {}", key, val);
            return JSONHelper.toJavaObject(val, valueType);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null && jedis.isConnected()) {
                jedis.close();
            }
        }

        return null;
    }

    @Override
    public V remove(K k) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = getActualKey(k);
            String val = jedis.get(key);
            V v = JSONHelper.toJavaObject(val, valueType);
            jedis.del(key);
            logger.info("remove key: {}, val: {}", key, val);
            return v;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (jedis != null && jedis.isConnected()) {
                jedis.close();
            }
        }
        return null;
    }
}
