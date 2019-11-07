package com.boot.common.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public abstract class EncryptHelper {

    public static String KEY_MD5 = "MD5";
    public static String KEY_SHA = "SHA";

    /**
     * encryption with MD5
     *
     * @param data raw data in form of byte array
     * @return encrypted byte array
     * @throws Exception NoSuchAlgorithmException
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        if (data == null || data.length == 0) {
            return null;
        }
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * encryption with MD5
     *
     * @param sourceString raw String
     * @return String
     */
    public static String encryptMD5(String sourceString) {
        String resultString = null;
        if (sourceString == null || "".equals(sourceString.trim())) {
            return sourceString;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(KEY_MD5);
            // encryption
            byte[] b = md.digest(sourceString.getBytes(StandardCharsets.UTF_8));
            resultString = byte2hexString(b);
        } catch (Exception ex) {
        }
        return resultString;
    }

    /**
     * encryption with SHA
     *
     * @param data raw data in form of byte array
     * @return encrypted byte array
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        if (data == null || data.length == 0) {
            return null;
        }
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

    /**
     * encryption with SHA
     *
     * @param sourceString raw String
     * @return encrypted String
     */
    public static String encryptSHA(String sourceString) {
        String resultString = null;
        if (sourceString == null || "".equals(sourceString.trim())) {
            return sourceString;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(KEY_SHA);
            // encryption
            byte[] b = md.digest(sourceString.getBytes(StandardCharsets.UTF_8));
            resultString = byte2hexString(b);
        } catch (Exception ex) {
        }
        return resultString;
    }

    /**
     * convert byte array to String
     *
     * @param bytes input byte array
     * @return String
     */
    private static String byte2hexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                builder.append("0");
            }
            builder.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return builder.toString();
    }
}
