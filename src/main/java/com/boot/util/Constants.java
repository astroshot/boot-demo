package com.boot.util;

public abstract class Constants {

    public static final String EMAIL_REGEXP = "^[^@]+?@[0-9a-zA-z\\-_]+?(\\.[0-9a-zA-z]+?){1,2}";

    /**
     * 路径匹配：xxx/xx-xx_1123/path
     */
    public static final String PATH_URL_REGEXP = "([A-Za-z\\-_0-9]+)?((\\/[A-Za-z\\-_0-9]+)*)" +
            "(\\?(([A-Za-z\\-_]+)=([^\\s]*))(&(([A-Za-z\\-_]+)=([^\\s]*)))*)*";

}
