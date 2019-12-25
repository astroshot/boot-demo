package com.boot.ut.common.helper;

import com.boot.common.helper.AESHelper;
import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

public class AESHelperTest extends AbstractTestCase {

    @Test
    public void testEncryptHex() throws Exception {
        String secret = "1234123412341234";
        String content = "我本山中人，筑室依涧冈";
        AESHelper aes = new AESHelper(secret);
        String encryptedHex = aes.encryptHex(content);
        String decryptStr = aes.decryptHex(encryptedHex);
        logger.info("encrypted content: {}, decrypted content: {}", encryptedHex, decryptStr);
        Assert.assertEquals(content, decryptStr);
    }

    @Test
    public void testEncryptBase64() throws Exception {
        String secret = "1234123412341234";
        String content = "我本山中人，筑室依涧冈";
        AESHelper aes = new AESHelper(secret);
        String encryptedHex = aes.encryptBase64(content);
        String decryptStr = aes.decryptBase64(encryptedHex);
        logger.info("encrypted content: {}, decrypted content: {}", encryptedHex, decryptStr);
        Assert.assertEquals(content, decryptStr);
    }
}
