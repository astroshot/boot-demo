package com.boot.common.rpc.http.proxy.handler;

import com.boot.common.constant.CommonConstant;
import com.boot.common.helper.HTTPHelper;
import com.boot.common.rpc.http.annotation.HTTPRequest;
import com.boot.common.rpc.http.annotation.PathParam;
import com.boot.common.rpc.http.annotation.RequestBody;
import com.boot.common.rpc.http.annotation.RequestHeader;
import com.boot.common.rpc.http.annotation.RequestParam;
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

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * RPC invocation based on HTTP.
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
            return null;
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
                    RequestParam reqParam = p.getAnnotation(RequestParam.class);
                    if (reqParam == null || reqParam.inHeader()) {
                        continue;
                    }

                    Collection c = (Collection) args[i];

                    Iterator iter = c.iterator();
                    while (iter.hasNext()) {
                        Object obj = iter.next();
                        if (obj instanceof BaseDTO) {
                            paramMap.putAll(convertToMap((BaseDTO) obj));
                        } else {
                            String key = reqParam.value();
                            String val = convertValue(obj);
                            if (paramMap.containsKey(key)) {
                                paramMap.put(key, paramMap.get(key) + "," + val);
                            } else {
                                paramMap.put(key, val);
                            }
                        }
                    }
                } else if (args[i] instanceof BaseDTO) {
                    paramMap.putAll(convertToMap((BaseDTO) args[i]));
                } else if (args[i] instanceof String || isWrapClass(args[i].getClass())) {
                    RequestParam reqParam = p.getAnnotation(RequestParam.class);
                    if (reqParam == null || reqParam.inHeader()) {
                        continue;
                    }

                    String val = args[i] == null ? "" : (args[i] + "");
                    paramMap.put(reqParam.value(), val);
                } else {
                    throw new IllegalArgumentException("Unsupported param type: " + args[i].getClass());
                }
            }
        }
        return args[0];
    }

    protected Map<String, String> getHeader(Method method, Object[] args) throws Throwable {
        Map<String, String> header = new HashMap<>();

        RequestHeader[] requestHeaders = method.getDeclaringClass().getAnnotationsByType(RequestHeader.class);
        if (requestHeaders != null || requestHeaders.length > 0) {
            for (RequestHeader item : requestHeaders) {
                header.put(item.key(), item.value());
            }
        }

        requestHeaders = method.getAnnotationsByType(RequestHeader.class);
        if (requestHeaders != null || requestHeaders.length > 0) {
            for (RequestHeader item : requestHeaders) {
                header.put(item.key(), item.value());
            }
        }

        Parameter[] parameters = method.getParameters();
        if (parameters == null || parameters.length < 1) {
            return header;
        }

        for (int i = 0; i < parameters.length; i++) {
            Parameter p = parameters[i];
            Object arg = args[i];
            if (p == null || arg == null) {
                continue;
            }

            RequestBody requestBody = p.getAnnotation(RequestBody.class);
            if (requestBody != null) {
                if (!requestBody.header()) {
                    continue;
                }
                header.putAll(convertToMap((BaseDTO) args[0]));
            } else if ((arg instanceof String || isWrapClass(arg.getClass()))) {
                RequestParam requestParam = p.getAnnotation(RequestParam.class);
                if (requestParam != null && requestParam.inHeader()) {
                    String val = arg + "";
                    header.put(requestParam.value(), val);
                }
            } else {
                throw new IllegalArgumentException("Unsupported param type: " + args[i].getClass());
            }
        }

        return header;
    }

    protected String doRequest(RequestMethod requestMethod, String requestUrl, Map<String, String> header, Object param) {

        String response = "";
        long start = System.currentTimeMillis();
        try {
            if (RequestMethod.GET == requestMethod) {
                response = HTTPHelper.get(requestUrl, header, (Map<String, String>) param, null);
            } else if (RequestMethod.POST_FORM == requestMethod) {
                response = HTTPHelper.postForm(requestUrl, header, (Map<String, String>) param, null);
            } else if (RequestMethod.POST_JSON == requestMethod) {
                response = HTTPHelper.postJson(requestUrl, header, param, null);
            } else {
                logger.error("Unsupported method: {}", requestMethod);
                throw new RuntimeException("Unsupported method");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            long end = System.currentTimeMillis();
            logger.info("RPC invocation: url: {}, method: {}, header: {}, pars: {}, result: {}, cost: {}ms", requestUrl,
                    requestMethod.name(), toJsonString(header), toJsonString(param), response, end - start);
        }
        return response;
    }

    @Override
    protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {

        Class<?> returnType = method.getReturnType();
        HTTPRequest request = method.getAnnotation(HTTPRequest.class);

        if ("".equals(request.url()) && "".equals(request.urlKey())) {
            throw new IllegalArgumentException("URL should be configured.");
        }

        if (!("void".equals(returnType.getName())
                || returnType.equals(Future.class)
                || Future.class.isAssignableFrom(returnType))) {
            throw new IllegalArgumentException("不支持的返回值");
        }


        String url = getRequestUrl(method, args, request);
        RequestMethod requestMethod = request.method();
        Object param = getParameters(method, args);
        Map<String, String> header = getHeader(method, args);
        String responseStr = doRequest(requestMethod, url, header, param);
        return convert(responseStr, method);
    }

    protected <T> T convert(String responseStr, Method method) throws IOException {
        if ("void".equals(method.getReturnType().getName())) {
            return null;
        } else if (String.class.equals(method.getReturnType())) {
            return (T) convert(responseStr, String.class);
        } else if (method.getGenericReturnType() instanceof ParameterizedType) {
            return (T) convert(responseStr, method.getGenericReturnType());
        } else {
            return (T) convert(responseStr, method.getReturnType());
        }
    }

    protected <T> T convert(String responseStr, Type type) throws IOException {
        return OBJECT_MAPPER.readValue(responseStr, TYPE_FACTORY.constructType(type));
    }

    protected <T> T convert(String responseStr, Class<T> clazz) throws IOException {
        if (String.class.equals(clazz)) {
            return (T) responseStr;
        } else {
            return OBJECT_MAPPER.readValue(responseStr, clazz);
        }
    }


    protected Map<String, String> convertToMap(BaseDTO model) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(model);
            Map<String, Object> map = OBJECT_MAPPER.readValue(json, HashMap.class);
            Map<String, String> res = new HashMap<>();
            map.forEach((k, v) -> res.put(k, convertValue(v)));
            return res;
        } catch (Exception e) {
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
            throw new IllegalArgumentException("Only basic type and wrap class, String, null are supported");
        }
    }

    protected String toJsonString(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

}
