package com.boot.common.helper;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 用单个调度线程来按毫秒更新时间戳，相当于维护一个全局缓存。
 * 其他线程取时间戳时相当于从内存取，不会再造成时钟资源的争用，代价就是牺牲了一些精确度。
 * usage: `TimeHelper.getInstance.now()`
 */
public class TimeHelper {

    private volatile long timestamp;

    private TimeHelper() {
        this.timestamp = System.currentTimeMillis();
        scheduleTick();
    }

    private void scheduleTick() {
        new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "current-time-millisecond");
            thread.setDaemon(true);
            return thread;
        }).scheduleAtFixedRate(() -> timestamp = System.currentTimeMillis(), 1, 1, TimeUnit.MILLISECONDS);
    }

    public long now() {
        return timestamp;
    }

    public static TimeHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final TimeHelper INSTANCE = new TimeHelper();
    }
}
