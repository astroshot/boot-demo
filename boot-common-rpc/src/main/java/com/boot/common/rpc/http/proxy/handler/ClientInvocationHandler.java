package com.boot.common.rpc.http.proxy.handler;

import com.boot.common.constant.CommonConstant;
import com.boot.common.rpc.http.annotation.HTTPRequest;
import com.boot.common.rpc.http.annotation.PathParam;
import com.boot.common.rpc.http.annotation.RequestHeader;
import com.boot.common.rpc.http.annotation.RequestBody;
import com.boot.common.rpc.http.constant.RequestMethod;
import com.boot.common.rpc.model.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.reflect.AbstractInvocationHandler;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * RPC invocation based on HTTP.
 * TODO: to be finished
 */
public class ClientInvocationHandler extends AbstractInvocationHandler {

    private Environment env;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * time out: 30 seconds
     */
    protected static final int TIMEOUT = 30000;

    protected static final RequestConfig REQUEST_CONFIG;

    private static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(CommonConstant.DEFAULT_DATE_FORMAT));

        REQUEST_CONFIG = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                // socket IO timeout
                .setSocketTimeout(TIMEOUT)
                // redirect enabled by default
                .setRedirectsEnabled(true).build();
    }

    public ClientInvocationHandler(Environment env) {
        this.env = env;
    }

    /**
     * Tell a clz is wrapped class by jdk
     *
     * @param clz class
     * @return true if clz is Wrapped class, such as Short, Integer, Long, Double, Float, Byte, Boolean else false.
     */
    protected boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Get request url with possible path param
     *
     * @param method  invocation method
     * @param args    method parameters
     * @param request annotation @HTTPRequest
     * @return request url
     */
    protected String getRequestUrl(Method method, Object[] args, HTTPRequest request) {

        String url = request.url();

        if ("".equals(url)) {
            url = env.getProperty(request.urlKey());
        }

        if (url == null || "".equals(url)) {
            throw new IllegalArgumentException("URL config not found: " + request.urlKey());
        }

        Parameter[] parameters = method.getParameters();
        if (parameters != null && parameters.length > 0) {
            for (int i = 0, len = parameters.length; i < len; i++) {
                Parameter p = parameters[i];
                if (args[i] == null) {
                    continue;
                }
                if ((args[i] instanceof String || isWrapClass(args[i].getClass()))) {
                    PathParam pathVariable = p.getAnnotation(PathParam.class);
                    if (pathVariable != null) {
                        String value = args[i] == null ? "" : (args[i] + "");
                        url = url.replace(String.format("{%s}", pathVariable.value()), value);
                    }
                }
            }
        }
        return url;
    }

    protected Object getParameters(Method method, Object[] args) throws Throwable {
        Map<String, String> paramMap = null;
        Parameter[] parameters = method.getParameters();

        HTTPRequest request = method.getAnnotation(HTTPRequest.class);
        RequestMethod requestMethod = request.method();

        if (parameters == null || parameters.length < 1) {
            return paramMap;
        }

        if (requestMethod == RequestMethod.GET || requestMethod == RequestMethod.POST_FORM) {
            paramMap = new HashMap<>();
            for (int i = 0; i < parameters.length; i++) {
                Parameter p = parameters[i];
                if (p == null || args[i] == null || p.getAnnotation(RequestHeader.class) != null) {
                    continue;
                }

                RequestBody requestBody = p.getAnnotation(RequestBody.class);
                if (requestBody != null && !requestBody.header()) {
                    paramMap.putAll(convertToMap((BaseDTO) args[0]));
                }

                if (args[i] instanceof Collection) {

                }
            }
        }
        return null;
    }

    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {

        Class<?> returnType = method.getReturnType();
        HTTPRequest request = method.getAnnotation(HTTPRequest.class);

        if ("".equals(request.url()) && "".equals(request.urlKey())) {
            throw new IllegalArgumentException("URL should be configured.");
        }

        String url = getRequestUrl(method, args, request);
        RequestMethod requestMethod = request.method();
        return null;
    }

    protected Map<String, String> convertToMap(BaseDTO model) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(model);
            Map<String, Object> map = OBJECT_MAPPER.readValue(json, HashMap.class);
            Map<String, String> res = new HashMap<>();
            map.forEach((k, v) -> res.put(k, convertValue(v)));
            return res;
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return new HashMap<>();
    }

    protected String convertValue(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof String || isWrapClass(obj.getClass())) {
            return obj + "";
        } else {
            throw new IllegalArgumentException("参数类型只支持八种基本数据类型以及对应的包装类、String、null");
        }
    }

}
