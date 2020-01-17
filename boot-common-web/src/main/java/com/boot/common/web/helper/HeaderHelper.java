package com.boot.common.web.helper;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class HeaderHelper {

    public static String getHeaderValueByName(HttpServletRequest request, String name) {
        return request.getHeader(name);
    }

    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> header = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String val = request.getHeader(key);
            header.put(key, val);
        }
        return header;
    }
}
