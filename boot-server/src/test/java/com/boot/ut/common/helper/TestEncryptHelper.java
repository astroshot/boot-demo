package com.boot.ut.common.helper;

import com.boot.common.helper.EncryptHelper;
import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

public class TestEncryptHelper extends AbstractTestCase {

    @Test
    public void testEncryptMD5() {
        String rawStr = "abcdefg";
        String encryptedStr = EncryptHelper.encryptMD5(rawStr);
        logger.info("Encrypted String: {}", encryptedStr);
        Assert.assertEquals("7ac66c0f148de9519b8bd264312c4d64", encryptedStr);
    }
}
