package com.boot.common.helper;

import com.boot.common.constant.CommonConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public abstract class DateHelper {

    /**
     * Get UTC datetime String in given format
     *
     * @param date   date to convert
     * @param format date time format
     * @return UTC date time String
     */
    public static String getUTCDateTime(Date date, String format) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public static String getUTCDateTime(Date date) {
        return getUTCDateTime(date, CommonConstant.DEFAULT_DATE_FORMAT);
    }

    public static String getUTCDateTime() {
        return getUTCDateTime(new Date());
    }
}
