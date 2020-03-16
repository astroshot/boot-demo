package com.boot.ut.common.helper;

import com.boot.common.web.helper.IPHelper;
import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

public class IpHelperTest extends AbstractTestCase {

    @Test
    public void testConvertIpToInt() {
        String testIp = "192.168.0.1";
        int ip = IPHelper.convertIpToInt(testIp);
        logger.info("ip: {}", ip);
        Assert.assertEquals(-1062731775, ip);
    }
}
