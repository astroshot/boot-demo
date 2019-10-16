package com.boot.common.web.filter.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XSSHTTPServletRequestWrapper extends HttpServletRequestWrapper {

    HttpServletRequest orgRequest = null;

    public XSSHTTPServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    /**
     * 覆盖 getParameter 方法，将参数名和参数值都做 XSS 过滤
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 覆盖 getParameterValues 方法，将参数名和参数值都做 XSS 过滤
     */
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = xssEncode(values[i]);
        }
        return encodedValues;
    }

    /**
     * 获取 request 的属性时，做 XSS 过滤
     */
    @Override
    public Object getAttribute(String name) {
        Object value = super.getAttribute(name);
        if (null != value && value instanceof String) {
            value = xssEncode((String) value);
        }
        return value;
    }

    /**
     * 覆盖 getHeader 方法，将参数名和参数值都做 XSS 过滤
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (value != null) {
            value = xssEncode(value);
        }
        return value;
    }

    /**
     * 将容易引起 XSS 漏洞的半角字符直接替换成全角字符
     *
     * @param s
     * @return
     */
    private static String xssEncode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        try {
            s = s.replace("<", "&lt;");
            s = s.replace(">", "&gt;");
            return s;
        } catch (NullPointerException e) {
            return s;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取最原始的 request
     *
     * @return
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的 request 的静态方法
     *
     * @return
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XSSHTTPServletRequestWrapper) {
            return ((XSSHTTPServletRequestWrapper) req).getOrgRequest();
        }

        return req;
    }
}
