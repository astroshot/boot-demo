package com.boot.common.helper;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.Pair;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


public abstract class RSAHelper {

    public static final String KEY_ALGORITHM = "RSA";

    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";

    private static final String PRIVATE_KEY = "RSAPrivateKey";

    public static final int DEFAULT_KEY_SIZE = 1024;

    /**
     * 使用私钥对信息进行数字签名
     *
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return 签名
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由 base64 编码的私钥
        byte[] keyBytes = Base64Helper.decode(privateKey);

        // 构造 PKCS8EncodedKeySpec 对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);

        return Base64Helper.encode(signature.sign());
    }

    public static String sign(String data, String privateKey) throws Exception {
        return sign(data.getBytes(StandardCharsets.UTF_8), privateKey);
    }

    /**
     * 验证数字签名
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @param sign      签名
     * @return boolean
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64Helper.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);

        // 验证签名是否正常
        return signature.verify(Base64Helper.decode(sign));
    }

    public static boolean verify(String data, String publicKey, String sign) throws Exception {
        return verify(data.getBytes(StandardCharsets.UTF_8), publicKey, sign);
    }

    /**
     * 使用私钥加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 字符串形式的密文
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String key) throws Exception {
        return Base64.encodeBase64String(encryptByPrivateKey(data.getBytes(StandardCharsets.UTF_8), key));
    }

    /**
     * 使用私钥加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 字节数组形式的密文
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64.decodeBase64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static String encryptByPrivateKey(String content, PrivateKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encodeBase64String(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 使用私钥解密数据
     *
     * @param data 密文
     * @param key  密钥
     * @return 字符串形式的原文
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String key) throws Exception {
        return new String(decryptByPrivateKey(Base64.decodeBase64(data), key));
    }

    /**
     * 使用私钥解密数据
     *
     * @param data 密文
     * @param key  密钥
     * @return 字节数组形式的原文
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Helper.decode(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    public static String decryptByPrivateKey(String encryptedData, PrivateKey key) throws Exception {
        byte[] data = Base64.decodeBase64(encryptedData);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(data));
    }

    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
        // 对公钥解码
        byte[] keyBytes = Base64.decodeBase64(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static String encryptByPublicKey(String data, String key) throws Exception {
        return Base64.encodeBase64String(encryptByPublicKey(data.getBytes(StandardCharsets.UTF_8), key));
    }

    public static String encryptByPublicKey(String content, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encodeBase64String(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decryptByPublicKey(String data, String key) throws Exception {
        return new String(decryptByPublicKey(Base64.decodeBase64(data), key));
    }

    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = Base64Helper.decode(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static String decryptByPublicKey(String encryptedData, PublicKey key) throws Exception {
        byte[] data = Base64.decodeBase64(encryptedData);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(data));
    }

    public static String getPrivateKey(Pair<PublicKey, PrivateKey> keyPair) {
        return Base64.encodeBase64String(keyPair.getRight().getEncoded());
    }

    public static String getPublicKey(Pair<PublicKey, PrivateKey> keyPair) {
        return Base64.encodeBase64String(keyPair.getLeft().getEncoded());
    }

    public static String getPrivateKeyStr(Map<String, Key> keyMap) throws Exception {
        Key key = keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    public static String getPublicKeyStr(Map<String, Key> keyMap) throws Exception {
        Key key = keyMap.get(PUBLIC_KEY);
        return Base64Helper.encode(key.getEncoded());
    }

    public static Pair<PublicKey, PrivateKey> makeKeyPair(int keySize) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generator.initialize(keySize);

        KeyPair keyPair = generator.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return Pair.of(publicKey, privateKey);
    }

    public static Pair<PublicKey, PrivateKey> makeKeyPair() throws Exception {
        return makeKeyPair(DEFAULT_KEY_SIZE);
    }

    public static Map<String, Key> makeKeyMap(int keySize) throws Exception {
        Pair<PublicKey, PrivateKey> keyPair = makeKeyPair(keySize);
        Map<String, Key> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY, keyPair.getLeft());
        keyMap.put(PRIVATE_KEY, keyPair.getRight());
        return keyMap;
    }

    public static PublicKey getPublicKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PUBLIC_KEY);
        return (PublicKey) key;
    }

    public static PrivateKey getPrivateKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PRIVATE_KEY);
        return (PrivateKey) key;
    }

    public static int maxLength(int keySize) {
        return keySize / 8 - 11;
    }
}