package com.boot.common.web.model;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Map;


@Builder
@Data
@ToString(includeFieldNames = true)
public class LogInfo {

    // normal or exception
    private String logType;

    // client IP
    private String clientIP;

    // request start timestamp
    private long startTime;

    private String userCode;

    private String uri;

    private String httpMethod;

    private String serviceName;

    private String methodName;

    // http request parameters
    private String requestParams;

    // http request body
    private String requestBody;

    private String exceptionInfo;

    private String exceptionDetail;

    // http response content
    private String responseContent;

    private String clientVersion;

    private String serverVersion;

    // http status code
    private Integer status;

    // http request cost time
    private long cost;

    private Object[] methodParams;

    // http header
    private Map<String, String> header;

    private Map<String, String> ext;

    public String toJsonString() {
        return JSONObject.toJSONString(this,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.IgnoreNonFieldGetter);
    }
}
