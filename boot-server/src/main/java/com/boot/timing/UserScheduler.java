package com.boot.timing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class UserScheduler implements Runnable {

    private static volatile long AVAILABLE_TIME = 0;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ScheduledExecutorService scheduledExecutorService = null;

    public static boolean isActive() {
        return AVAILABLE_TIME - System.currentTimeMillis() > 0;
    }

    public static void setAvailableTime(long availableTime) {
        AVAILABLE_TIME = availableTime;
    }

    @PostConstruct
    public void init() {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(this, 5, 10, TimeUnit.SECONDS);
        scheduledExecutorService.submit(this);
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    @PreDestroy
    public void shutdown() {
        logger.info("jvm shutdown ...");
        setAvailableTime(0L);
        try {
            scheduledExecutorService.shutdown();
            while (!scheduledExecutorService.awaitTermination(100L, TimeUnit.MILLISECONDS)) {
                logger.info("waiting for job done... waiting for executorService shutdown...");
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        if (!isActive()) {
            return;
        }
        doYourWork();
    }

    protected void doYourWork() {

    }
}
