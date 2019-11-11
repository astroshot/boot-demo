package com.boot.common.web.helper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public abstract class CookieHelper {


    /**
     * Get cookies from request and put them in HashMap.
     * An empty Map will be returned if no cookies found.
     *
     * @param request HttpServletRequest
     * @return Map<String, Cookie>
     */
    public static Map<String, Cookie> getCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return cookieMap;
        }

        for (Cookie item : cookies) {
            cookieMap.put(item.getName(), item);
        }
        return cookieMap;
    }


    /**
     * Get Cookie from request by its name.
     *
     * @param request    HttpServletRequest
     * @param cookieName Cookie name
     * @return Cookie or null
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Map<String, Cookie> cookieMap = getCookieMap(request);

        if (cookieMap.containsKey(cookieName)) {
            return cookieMap.get(cookieName);
        }

        return null;
    }


    /**
     * Get Cookie Value from request by its name.
     *
     * @param request    HttpServletRequest
     * @param cookieName Cookie name
     * @return Cookie Value or null
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = getCookie(request, cookieName);
        if (cookie == null) {
            return null;
        }

        return cookie.getValue();
    }
}
