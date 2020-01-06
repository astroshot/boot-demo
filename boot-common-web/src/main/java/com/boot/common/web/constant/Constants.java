package com.boot.common.web.constant;


public abstract class Constants {

    /**
     * 验证码的超时时间在 session 中存的 key
     * */
    public static final String CAPTCHA_TIME = "captcha-time";

    /**
     * 验证码在 session 中存的 key
     */
    public static final String CAPTCHA_STR = "captcha";

    /**
     * 验证码超时时间
     */
    public static final long CAPTCHA_TIME_OUT = 30 * 1000;

    public static final String REQUEST_PARAMETERS = "REQUEST_PARAMETERS";
}
