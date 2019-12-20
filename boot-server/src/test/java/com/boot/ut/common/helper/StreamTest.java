package com.boot.ut.common.helper;

import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StreamTest extends AbstractTestCase {

    @Test
    public void testStream() {
        String concatStr = Stream.of("a", "b", "c", "d").collect(Collectors.joining(", "));
        logger.info(concatStr);
        Assert.assertEquals("a, b, c, d", concatStr);
    }
}
