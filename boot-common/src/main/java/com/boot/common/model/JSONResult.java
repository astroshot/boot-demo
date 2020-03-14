package com.boot.common.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Controller 层返回结果 JSON 序列化的工具类
 *
 * @param <T> VO class 类型
 */
public class JSONResult<T> implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONResult.class);

    private static final long serialVersionUID = -24395699114309708L;

    // 限制日志长度为 1024
    private static final int DEFAULT_MAX_LOG_LENGTH = 1024;

    public static final int SUCCESS = 0;

    public static final int ERROR = -1;

    private int code;
    private T data;
    private String msg;

    public JSONResult() {
    }

    public JSONResult(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static <T> JSONResult<T> create(int code, T data, String msg) {
        return new JSONResult<>(code, data, msg);
    }

    public static <T> JSONResult<T> success(T data) {
        JSONResult<T> res = create(JSONResult.SUCCESS, data, "Success");
        if (LOGGER.isDebugEnabled()) {
            String logString = res.toString();
            if (logString.length() > DEFAULT_MAX_LOG_LENGTH) {
                logString = logString.substring(0, DEFAULT_MAX_LOG_LENGTH);
            }
            LOGGER.debug(logString);
        }
        return res;
    }

    public static <T> JSONResult<T> success() {
        return success(null);
    }

    public static <T> JSONResult<T> error(T data, String msg) {
        return create(JSONResult.ERROR, data, msg);
    }

    public static <T> JSONResult<T> error(T data) {
        return create(JSONResult.ERROR, data, "Failed");
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

    public String toJsonString() {
        return JSONObject
                .toJSONString(this, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue,
                        SerializerFeature.DisableCircularReferenceDetect);
    }
}
