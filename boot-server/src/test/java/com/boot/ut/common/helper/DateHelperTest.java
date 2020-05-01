package com.boot.ut.common.helper;

import com.boot.common.helper.DateHelper;
import com.boot.ut.common.AbstractTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DateHelperTest extends AbstractTestCase {

    @Test
    public void testGetFirstDay() {
        Date date = DateHelper.getFirstDayOfMonth(2020, 4);
        Date testDate = DateHelper.parseDate("2020-04-01 00:00:00:000", DateHelper.YMDHMSMS);
        logger.info("Date: {}", date);
        Assert.assertEquals(testDate, date);
    }

    @Test
    public void testLastDayOfYearMonth() {
        Date date = DateHelper.getLastDayOfMonth(2020, 3);
        Date testDate = DateHelper.parseDate("2020-03-31 23:59:59:999", DateHelper.YMDHMSMS);
        logger.info("Date: {}", date);
        Assert.assertEquals(testDate, date);
    }
}
