package com.boot.common.web.resolver;

public class WebCommonException extends RuntimeException{

    private int code;

    public WebCommonException(int code) {
        this.code = code;
    }

    public WebCommonException(String message, int code) {
        super(message);
        this.code = code;
    }

    public WebCommonException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public WebCommonException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public WebCommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                              int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
