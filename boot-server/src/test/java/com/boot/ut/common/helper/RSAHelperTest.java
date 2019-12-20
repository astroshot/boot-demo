package com.boot.ut.common.helper;

import com.boot.common.helper.RSAHelper;
import com.boot.ut.common.AbstractTestCase;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;


public class RSAHelperTest extends AbstractTestCase {

    @Test
    public void testEncryptByPublicKey() throws Exception {
        Pair<PublicKey, PrivateKey> keyPair = RSAHelper.makeKeyPair();
        String publicKey = RSAHelper.getPublicKey(keyPair);
        String privateKey = RSAHelper.getPrivateKey(keyPair);

        String content = "人生如逆旅，我亦是行人";
        String encryptedData = RSAHelper.encryptByPublicKey(content, publicKey);
        logger.info("public key: {}, private key: {}, encrypted data: {}", publicKey, privateKey, encryptedData);
        String decryptedData = RSAHelper.decryptByPrivateKey(encryptedData, privateKey);
        Assert.assertEquals(content, decryptedData);
    }

    @Test
    public void testEncryptByPrivateKey() throws Exception {
        Pair<PublicKey, PrivateKey> keyPair = RSAHelper.makeKeyPair();
        String publicKey = RSAHelper.getPublicKey(keyPair);
        String privateKey = RSAHelper.getPrivateKey(keyPair);

        String content = "人生如逆旅，我亦是行人";
        String encryptedData = RSAHelper.encryptByPrivateKey(content, privateKey);
        logger.info("public key: {}, private key: {}, encrypted data: {}", publicKey, privateKey, encryptedData);
        String decryptedData = RSAHelper.decryptByPublicKey(encryptedData, publicKey);
        Assert.assertEquals(content, decryptedData);
    }

    @Test
    public void testSign() throws Exception {
        String content = "人生如逆旅，我亦是行人";
        Pair<PublicKey, PrivateKey> keyPair = RSAHelper.makeKeyPair();
        String publicKey = RSAHelper.getPublicKey(keyPair);
        String privateKey = RSAHelper.getPrivateKey(keyPair);
        String sign = RSAHelper.sign(content, privateKey);
        logger.info("public key: {}, private key: {}, sign: {}", publicKey, privateKey, sign);
        Assert.assertTrue(RSAHelper.verify(content, publicKey, sign));
    }
}
