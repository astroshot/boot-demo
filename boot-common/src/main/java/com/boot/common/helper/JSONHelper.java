package com.boot.common.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * JSONHelper does conversion between JSON String and Java Object.
 */
public abstract class JSONHelper {

    private static final Logger logger = LoggerFactory.getLogger(JSONHelper.class);

    /**
     * Convert JSON String to Java Object
     *
     * @param jsonStr JSON String
     * @param type    type
     * @param <T>     type
     * @return instance of T
     */
    public static <T> T toJavaObject(String jsonStr, Type type) {
        if (jsonStr == null || "".equals(jsonStr) || type == null) {
            return null;
        }

        try {
            return JSONObject.parseObject(jsonStr, type);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * Convert JSON String to Java Object
     *
     * @param jsonStr   JSON String
     * @param reference generic type reference
     * @param <T>       type
     * @return instance of T
     */
    public static <T> T toJavaObject(String jsonStr, TypeReference<T> reference) {
        if (jsonStr == null || "".equals(jsonStr) || reference == null) {
            return null;
        }

        try {
            return JSONObject.parseObject(jsonStr, reference);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * Convert JSON String to Java Object
     *
     * @param jsonStr JSON String
     * @param clz     class of type T
     * @param <T>     type
     * @return instance of T
     */
    public static <T> T toJavaObject(String jsonStr, Class<T> clz) {
        if (jsonStr == null || "".equals(jsonStr)) {
            return null;
        }

        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        if (jsonObject == null) {
            return null;
        }

        try {
            return JSONObject.toJavaObject(jsonObject, clz);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * Convert Java Object to JSON String
     *
     * @param obj Java Object
     * @return JSON String
     */
    public static String toJSONString(Object obj) {
        if (obj == null) {
            return null;
        }

        return JSONObject.toJSONString(
                obj,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect
        );
    }

    /**
     * Convert Collection to JSON String
     *
     * @param collection @see java.util.Collection
     * @return JSON String
     */
    public static String toJSONString(Collection collection) {
        return JSONArray.toJSONString(
                collection,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect
        );
    }

}
