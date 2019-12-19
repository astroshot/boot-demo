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

    /**
     * Convert timestamp to java.util.Date
     *
     * @param timestamp timestamp in ms
     * @return Date
     */
    public static Date convertToDate(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * Convert timestamp to String in given format
     *
     * @param timestamp timestamp in ms
     * @param format    datetime format
     * @return Date in String
     */
    public static String convertToDateString(long timestamp, String format) {
        Date date = new Date(timestamp);
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * Convert timestamp to String in default format `yyyy-MM-dd HH:mm:ss`
     *
     * @param timestamp timestamp in ms
     * @return Date in String
     */
    public static String convertToDateString(long timestamp) {
        Date date = new Date(timestamp);
        return new SimpleDateFormat(CommonConstant.DEFAULT_DATE_FORMAT).format(date);
    }
}
