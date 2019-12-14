package com.boot.ut.common.helper;

import com.boot.common.helper.DESHelper;
import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

public class DESHelperTest extends AbstractTestCase {

    @Test
    public void testDES() throws Exception {
        DESHelper desHelper = DESHelper.getInstance("abcd");
        String encryptedStr = desHelper.encrypt("123456");
        // TODO: fix this
        Assert.assertEquals("U2FsdGVkX18gBgBt6bPtVQjCUiLz2jEg", encryptedStr);
    }
}
