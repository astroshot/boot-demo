package com.boot.common.dao.enums;


public enum UserStatus {

    NORMAL(0, "正常状态"),
    LOGOUT(1, "注销"),

    ;

    private int value;

    private String description;

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    UserStatus(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static UserStatus getByValue(Integer value) {
        if (value == null) {
            return null;
        }

        for (UserStatus item : values()) {
            if (item.value == value) {
                return item;
            }
        }

        return null;
    }
}
