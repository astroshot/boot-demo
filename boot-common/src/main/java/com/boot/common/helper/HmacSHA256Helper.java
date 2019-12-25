package com.boot.common.helper;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public abstract class HmacSHA256Helper {

    private static Logger logger = LoggerFactory.getLogger(HmacSHA256Helper.class);

    protected static final String ALGORITHM_NAME = "HmacSHA256";

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * 加密
     *
     * @param message 消息
     * @param secret  秘钥
     * @return 加密后字符串
     */
    public static String encrypt(String message, String secret) {
        String hash = "";
        try {
            Mac mac = Mac.getInstance(ALGORITHM_NAME);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), ALGORITHM_NAME);
            mac.init(secret_key);
            byte[] bytes = mac.doFinal(message.getBytes());
            hash = Hex.encodeHexString(bytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return hash;
    }


}