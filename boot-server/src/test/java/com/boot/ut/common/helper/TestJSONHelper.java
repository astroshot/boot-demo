package com.boot.ut.common.helper;

import com.boot.common.helper.JSONHelper;
import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestJSONHelper extends AbstractTestCase {

    @Test
    public void testToJSONString() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            list.add(i);
        }
        Assert.assertEquals("[0,1,2,3,4,5]", JSONHelper.toJSONString(list));

        Assert.assertEquals("null", JSONHelper.toJSONString(null));
        Assert.assertEquals("[]", JSONHelper.toJSONString(new ArrayList()));
    }
}
