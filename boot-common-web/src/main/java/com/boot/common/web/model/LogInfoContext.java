package com.boot.common.web.model;

public abstract class LogInfoContext {

    private static ThreadLocal<LogInfo.LogInfoBuilder> CONTEXT = new ThreadLocal<>();

    public static LogInfo.LogInfoBuilder getLogBuilder() {
        LogInfo.LogInfoBuilder log = CONTEXT.get();
        if (log == null) {
            log = LogInfo.builder().startTime(System.currentTimeMillis()).logType("NORMAL");
            CONTEXT.set(log);
        }
        return log;
    }

    public static void setLogBuilder(LogInfo.LogInfoBuilder log) {
        CONTEXT.set(log);
    }

    public static void remove() {
        CONTEXT.remove();
    }
}
