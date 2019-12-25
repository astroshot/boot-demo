package com.boot.ut.common.helper;

import com.boot.common.helper.Base64Helper;
import com.boot.ut.common.AbstractTestCase;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

public class Base64HelperTest extends AbstractTestCase {

    @Test
    public void testBase64() {
        String input = "Base64 编码字符串";
        String base64Str = Base64Helper.encode(input);
        String base64Str2 = Base64.encodeBase64String(input.getBytes());
        Assert.assertEquals("QmFzZTY0IOe8lueggeWtl+espuS4sg==", base64Str);

        String base64DecodeStr = Base64Helper.decodeToStr(base64Str);
        Assert.assertEquals(input, base64DecodeStr);
    }
}
