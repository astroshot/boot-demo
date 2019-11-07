package com.boot.ut.common.helper;

import com.boot.common.helper.RegExpHelper;
import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

public class TestRegExpHelper extends AbstractTestCase {

    @Test
    public void testMatch() {
        Assert.assertTrue(RegExpHelper.match("[0-9]", "4"));
        Assert.assertFalse(RegExpHelper.match("[0-9]", "o"));
    }
}
