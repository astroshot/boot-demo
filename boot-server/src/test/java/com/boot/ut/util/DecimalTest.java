package com.boot.ut.util;

import com.boot.ut.common.AbstractTestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalTest extends AbstractTestCase {

    @Test
    public void testDecimal() {
        double a = 1.1;
        double b = 1.123;
        a = a + b;
        BigDecimal bd = new BigDecimal(a);
        b = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
        logger.info("val: {}", b);

        b = bd.setScale(1, RoundingMode.HALF_UP).doubleValue();
        logger.info("val: {}", b);
    }
}
