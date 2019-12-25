package com.boot.ut.common.helper;

import com.boot.common.helper.HmacSHA256Helper;
import com.boot.ut.common.AbstractTestCase;
import org.apache.commons.codec.binary.Hex;
import org.junit.Assert;
import org.junit.Test;

public class HmacSHA256HelperTest extends AbstractTestCase {

    @Test
    public void test() {
        String content = "Java 是一门静态弱类型语言";
        String secret = "123456";

        String encryptedData = HmacSHA256Helper.encrypt(content, secret);
        logger.info("encrypted: {}", encryptedData);
        Assert.assertEquals("484afa1e8cf0cfc9181cb11a16c9f021f5d0630ddd4bc3788a72fc3b97d6d2d7", encryptedData);
    }

    @Test
    public void testByte() {
        String content = "Java 是一门静态弱类型语言";
        String hex1 = HmacSHA256Helper.byteArrayToHexString(content.getBytes());
        String hex2 = Hex.encodeHexString(content.getBytes());
        Assert.assertEquals(hex1, hex2);
    }
}
