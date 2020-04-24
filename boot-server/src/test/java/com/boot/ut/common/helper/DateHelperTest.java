package com.boot.ut.common.helper;

import com.boot.common.helper.DateHelper;
import com.boot.ut.common.AbstractTestCase;
import org.junit.Test;

import java.util.Date;

public class DateHelperTest extends AbstractTestCase {

    @Test
    public void testGetFirstDay() {
        Date date = DateHelper.getFirstDayOfMonth(2020, 4);
        logger.info("Date: {}", date);
    }
}
