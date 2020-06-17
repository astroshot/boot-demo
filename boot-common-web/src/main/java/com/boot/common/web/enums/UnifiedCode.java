package com.boot.common.web.enums;

public enum UnifiedCode implements ExceptionCode{

    /**
     * Unknown Exception
     */
    UNKNOWN(500, "系统异常"),

    /**
     * Parameter error
     */
    PARAMETER_ERROR(400, "参数不合法"),

    ;

    int code;
    String description;

    UnifiedCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
