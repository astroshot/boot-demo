package com.boot.common.helper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;


public class AESHelper {

    protected static final String ALGORITHM_NAME = "AES";

    /**
     * 加密算法/工作模式/填充方式
     */
    protected static final String ALGORITHM_MODE = "AES/ECB/PKCS5Padding";

    private String secret;

    private KeyGenerator keyGenerator;

    private SecretKeySpec secretKeySpec;

    private Cipher cipher;

    /**
     * @param secret encrypt key, should be 16/24/32 bytes
     * @throws Exception
     */
    public AESHelper(String secret) throws Exception {
        this.secret = secret;
        keyGenerator = KeyGenerator.getInstance(ALGORITHM_NAME);
        secretKeySpec = new SecretKeySpec(secret.getBytes(), ALGORITHM_NAME);
        cipher = Cipher.getInstance(ALGORITHM_MODE);
    }

    public byte[] encrypt(String content) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    public String encryptHex(String content) throws Exception {
        return Hex.encodeHexString(encrypt(content));
    }

    public String encryptBase64(String content) throws Exception {
        return Base64.encodeBase64String(encrypt(content));
    }

    public byte[] decrypt(byte[] data) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return cipher.doFinal(data);
    }

    public String decryptHex(String hexData) throws Exception {
        return new String(decrypt(Hex.decodeHex(hexData.toCharArray())));
    }

    public String decryptBase64(String base64Data) throws Exception {
        return new String(decrypt(Base64.decodeBase64(base64Data)));
    }
}
