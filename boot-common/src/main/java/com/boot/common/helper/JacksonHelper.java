package com.boot.common.helper;

import com.boot.common.constant.CommonConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public abstract class JacksonHelper {

    private static final Logger logger = LoggerFactory.getLogger(JacksonHelper.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static ObjectMapper transferMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.setDateFormat(new SimpleDateFormat(CommonConstant.DEFAULT_DATE_FORMAT));
        // objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        transferMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        transferMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        transferMapper.setDateFormat(new SimpleDateFormat(CommonConstant.DEFAULT_DATE_FORMAT));
        transferMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * JSON 字符串转 Java 对象
     *
     * @param jsonStr JSON String
     * @param type    泛型，如 List<User>, ``TypeReference type = new TypeReference<List<User>>() {}``
     * @param <T>     泛型类型，如 User
     * @return type 指定的 Java object
     */
    public static <T> T toJavaObject(String jsonStr, TypeReference<T> type) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        }

        try {
            return (T) objectMapper.readValue(jsonStr, type);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * Java object 转 JSON String
     *
     * @param object Java Object
     * @return JSON String
     */
    public static String toJSONString(Object object) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return jsonString;
    }

}
