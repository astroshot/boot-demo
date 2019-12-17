package com.boot.common.cache.service;


public interface CacheService<K, V> {

    /**
     * set cache
     *
     * @param k key
     * @param v value
     * @return true if success
     */
    boolean put(K k, V v);

    /**
     * set cache with expire time
     *
     * @param k          key
     * @param v          value
     * @param expireTime expireTime in seconds
     * @return true if success
     */
    boolean put(K k, V v, int expireTime);

    /**
     * get value from cache by key
     *
     * @param k key
     * @return value
     */
    V get(K k);

    /**
     * delete key in cache
     *
     * @param k key
     * @return V if k exists else null
     */
    V remove(K k);
}
