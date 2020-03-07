package com.boot.common.web.helper;

import com.boot.common.web.constant.Constants;
import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class RequestHelper {

    /**
     * @param request HTTP Servlet Request
     * @return true only if POST or PUT with JSON body
     */
    public static boolean isJSONBody(HttpServletRequest request) {
        if (request == null) {
            return false;
        }

        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }

        return contentType.contains(Constants.JSON_CONTENT)
                && (HttpMethod.POST.matches(request.getMethod()) || HttpMethod.PUT.matches(request.getMethod()));
    }

    /**
     * @param request HTTP Servlet Request
     * @return true only if POST with JSON body
     */
    public static boolean isFormBody(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }

        return (contentType.contains(Constants.FORM_CONTENT) && HttpMethod.POST.matches(request.getMethod()));
    }

    public static Charset getCharset(HttpServletRequest request) {
        String charset = request.getCharacterEncoding();
        if (charset != null && Charset.isSupported(charset)) {
            return Charset.forName(charset);
        }

        return StandardCharsets.UTF_8;
    }

    public static String getBody(HttpServletRequest request) throws IOException {
        return StreamUtils.copyToString(request.getInputStream(), getCharset(request));
    }

}
