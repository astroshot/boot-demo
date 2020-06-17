package com.boot.ut.common.helper;

import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StreamTest extends AbstractTestCase {

    @Test
    public void testStream() {
        String concatStr = Stream.of("a", "b", "c", "d").collect(Collectors.joining(", "));
        logger.info(concatStr);
        Assert.assertEquals("a, b, c, d", concatStr);
    }

    @Test
    public void testCopy() {
        String int1 = new String("123456789123456789");
        String int2 = new String("234567891234567891");

        List<String> intList = new ArrayList<>();
        intList.add(int1);
        intList.add(int2);

        List<String> filtered = intList.stream().filter(intVal -> intVal.toString().startsWith("1")).collect(Collectors.toList());
        List<String> copyed = new ArrayList<>(filtered);
        String origin = intList.get(0);
        String filterdInt = copyed.get(0);
        logger.info("{}", filterdInt);
    }
}
