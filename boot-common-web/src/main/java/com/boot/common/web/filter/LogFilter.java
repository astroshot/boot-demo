package com.boot.common.web.filter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.boot.common.context.ThreadLocalContextHolder;
import com.boot.common.web.constant.Constants;
import com.boot.common.web.helper.IPHelper;
import com.boot.common.web.helper.RequestHelper;
import com.boot.common.web.model.LogInfo;
import com.boot.common.web.model.LogInfoContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LogFilter extends OncePerRequestFilter {

    private static String HOSTNAME;

    static {
        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (Exception ignored) {
        }
    }

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        MDC.put("hostname", HOSTNAME);

        LogInfo.LogInfoBuilder builder = LogInfoContext.getLogBuilder();
        Enumeration<String> names = request.getParameterNames();
        List<String> keys = new ArrayList<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            keys.add(name);
        }

        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        Map<String, String[]> paramMap = new HashMap<>();
        for (String item : keys) {
            if (sb.length() > 0) {
                sb.append("&");
            }

            String[] values = request.getParameterValues(item);
            if (values == null) {
                values = new String[]{""};
            }

            for (String val : values) {
                sb.append(item).append("=").append(val);
            }
            paramMap.put(item, values);
        }

        ThreadLocalContextHolder.put(Constants.REQUEST_PARAMETERS, sb.toString());

        Enumeration<String> headerKeys = request.getHeaderNames();
        Map<String, String> header = new HashMap<>();
        while (headerKeys.hasMoreElements()) {
            String key = headerKeys.nextElement();
            if ("cookie".equalsIgnoreCase(key)) {
                header.put(key, request.getHeader(key)
                        .replaceAll("(.+?)(EXAMPLE=[^;]{64})([^;]*)(.*)", "$1$2$4"));
            } else {
                header.put(key, request.getHeader(key));
            }
        }

        String requestBody = null;
        if (request instanceof RequestBodyCacheWrapFilter) {
            requestBody = RequestHelper.getBody(request);
        }

        builder.clientIP(IPHelper.getIPAddress(request)).httpMethod(request.getMethod())
                .uri(URLDecoder.decode(request.getRequestURI()))
                .header(header)
                .requestBody(requestBody)
                .requestParams(convertMapToString(paramMap))
                .userCode("");

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            builder.logType("ERROR").exceptionInfo(e.getMessage());
            StringWriter writer = null;
            PrintWriter printer = null;

            try {
                writer = new StringWriter();
                printer = new PrintWriter(writer);
                builder.exceptionDetail(writer.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }

                    if (printer != null) {
                        printer.close();
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }

            throw e;
        } finally {
            long now = System.currentTimeMillis();
            long cost = now - builder.build().getStartTime();
            builder.cost(cost);
            logger.info(builder.build().toJsonString() + " " + cost + "ms");
            LogInfoContext.remove();
            ThreadLocalContextHolder.clear();
            MDC.clear();
        }
    }

    public String convertMapToString(Map<String, String[]> map) {
        return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
    }

}
