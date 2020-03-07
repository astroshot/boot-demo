package com.boot.common.web.interceptor;

import com.boot.common.helper.RSAHelper;
import com.boot.common.model.JSONResult;
import com.boot.common.web.annotation.RSASign;
import com.boot.common.web.constant.Constants;
import com.boot.common.web.filter.wrapper.RequestBodyCacheWrapper;
import com.boot.common.web.helper.RequestHelper;
import com.boot.common.web.helper.ResponseHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.StringJoiner;

/**
 * An interceptor is designed to verify HTTP request.
 */
public abstract class RSASignInterceptor implements HandlerInterceptor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        RSASign rsaSign = AnnotatedElementUtils.findMergedAnnotation(method, RSASign.class);
        if (rsaSign == null) {
            rsaSign = AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), RSASign.class);
        }

        if (rsaSign == null || !rsaSign.required()) {
            return true;
        }

        logger.info("sign key: {}", rsaSign.name());
        String signature = request.getParameter(rsaSign.name());
        if (StringUtils.isBlank(signature)) {
            signature = request.getHeader(rsaSign.name());
        }

        PublicKey key = getPublicKey();
        if (StringUtils.isBlank(signature) || key == null) {
            logger.error("Illegal signature");
            handleFailures(request, response, handler);
            return false;
        }

        // POST JSON
        if (RequestHelper.isJSONBody(request)) {
            if (!(request instanceof RequestBodyCacheWrapper)) {
                throw new UnsupportedOperationException("Unsupported RequestBody without Cache");
            }

            String requestBody = RequestHelper.getBody(request);
            String stamp = request.getHeader("stamp");
            String body = String.format("%s&%s", requestBody, stamp);
            boolean match = RSAHelper.verify(body, key, signature);
            logger.info("request body: {}, stamp: {}, verification: {}", requestBody, stamp, match);
            return match;
        }

        // POST FORM and GET
        Enumeration<String> names = request.getParameterNames();
        List<String> keys = new ArrayList<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            keys.add(name);
        }

        Collections.sort(keys);
        StringJoiner joiner = new StringJoiner("&");
        for (String keyItem : keys) {
            String[] values = request.getParameterValues(keyItem);
            if (values == null) {
                values = new String[]{""};
            }

            for (String val : values) {
                joiner.add(String.format("%s&%s", keyItem, val));
            }
        }

        joiner.add(request.getHeader("stamp"));
        String body = joiner.toString();
        boolean match = RSAHelper.verify(body, key, signature);
        logger.info("request parameters: {}, verification: {}", body, match);
        return match;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

    protected void handleFailures(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logger.warn("signature verify fails.");
        String res = JSONResult.create(-1, null, "Illegal signature").toJsonString();
        ResponseHelper.response(response, Constants.JSON_CONTENT, res);
    }

    public abstract PublicKey getPublicKey();

}
