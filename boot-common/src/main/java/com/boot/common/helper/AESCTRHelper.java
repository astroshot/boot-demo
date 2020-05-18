package com.boot.common.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public abstract class AESCTRHelper {

    private static final Logger logger = LoggerFactory.getLogger(AESCTRHelper.class);

    public static String decrypt(byte[] encryptedBytes, byte[] iv, String key) {
        byte[] decryptBytes = decryptBytes(encryptedBytes, iv, key);
        String decoded = "";
        if (decryptBytes.length <= 0) {
            return decoded;
        }

        decoded = new String(decryptBytes);
        return decoded.trim();
    }

    public static byte[] decryptBytes(byte[] encryptedBytes, byte[] iv, String key) {
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.US_ASCII);
            SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            return cipher.doFinal(encryptedBytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new byte[0];
    }
}
