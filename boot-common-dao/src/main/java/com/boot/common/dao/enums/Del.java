package com.boot.common.dao.enums;


public enum Del {

    NORMAL(0, "未删除"),
    DELETED(1, "已删除"),

    ;

    private int value;

    private String description;

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    Del(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
