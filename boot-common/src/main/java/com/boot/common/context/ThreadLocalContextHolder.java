package com.boot.common.context;

import java.util.HashMap;
import java.util.Map;

public abstract class ThreadLocalContextHolder {

    private static final ThreadLocal<Map<String, Object>> CONTEXT = ThreadLocal.withInitial(HashMap::new);

    public static Object get(String key) {
        if (CONTEXT.get() == null) {
            return null;
        }

        return CONTEXT.get().get(key);
    }

    public static Object put(String key, Object value) {
        Map<String, Object> localMap = CONTEXT.get();
        if (localMap == null) {
            localMap = new HashMap<>();
            CONTEXT.set(localMap);
        }

        return localMap.put(key, value);
    }

    public static Object remove(String key) {
        Map<String, Object> localMap = CONTEXT.get();
        if (localMap == null) {
            return null;
        }
        return localMap.remove(key);
    }

    public static void clear() {
        Map<String, Object> localMap = CONTEXT.get();
        if (localMap == null) {
            return;
        }
        localMap.clear();
    }
}
