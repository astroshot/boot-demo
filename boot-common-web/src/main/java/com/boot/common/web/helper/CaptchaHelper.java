package com.boot.common.web.helper;

import com.boot.common.web.constant.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class CaptchaHelper {

    /**
     * 获取验证码字符串并存放在session中
     *
     * @param request HTTP request
     * @return
     */
    public static String createCaptchaStr(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String str = System.currentTimeMillis() + "";
        session.setAttribute(Constants.CAPTCHA_TIME, str);
        str = str.substring(str.length() - 4);
        session.setAttribute(Constants.CAPTCHA_STR, str);
        return str;
    }

    /**
     * 验证验证码字符串是否输入正确
     *
     * @param request
     * @param str
     * @return
     */
    public static boolean checkCaptcha(HttpServletRequest request, String str) {
        HttpSession session = request.getSession();
        String captcha = (String) session.getAttribute(Constants.CAPTCHA_STR);
        return captcha != null && captcha.equals(str);
    }

    /**
     * 验证输入验证码字符串时间是否超时
     *
     * @param request HTTP request
     * @return true 超时，false 不超时
     */
    public static boolean checkTimeOut(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String timeStr = (String) session.getAttribute(Constants.CAPTCHA_TIME);
        long currentTime = System.currentTimeMillis();
        long createdAt = 0L;

        try {
            createdAt = Long.parseLong(timeStr);
        } catch (NumberFormatException e) {
            return true;
        }

        if (currentTime < createdAt) {
            return true;
        }

        return currentTime - createdAt > Constants.CAPTCHA_TIME_OUT;
    }
}
