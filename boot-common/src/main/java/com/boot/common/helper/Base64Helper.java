package com.boot.common.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

public abstract class Base64Helper {

    protected static Logger logger = LoggerFactory.getLogger(Base64Helper.class);

    public static String encode(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static String decode(String input) {
        return new String(Base64.getDecoder().decode(input));
    }
}
