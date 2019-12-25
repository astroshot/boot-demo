package com.boot.common.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

public abstract class Base64Helper {

    protected static Logger logger = LoggerFactory.getLogger(Base64Helper.class);

    public static String encode(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static String encode(String input) {
        return encode(input.getBytes());
    }

    public static String decodeToStr(String input) {
        return new String(decode(input));
    }

    public static byte[] decode(String input) {
        return Base64.getDecoder().decode(input);
    }
}
