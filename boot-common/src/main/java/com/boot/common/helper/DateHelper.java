package com.boot.common.helper;

import com.boot.common.constant.CommonConstant;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public abstract class DateHelper {

    private static final Logger logger = LoggerFactory.getLogger(DateHelper.class);

    public static String YMDHMSMS = "yyyy-MM-dd HH:mm:ss:SSS";

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

    public static Date parseDate(String dateStr, String dateFormat) {
        try {
            return DateUtils.parseDate(dateStr, dateFormat);
        } catch (ParseException e) {
            logger.error("convert error, date: {}, format: {}", dateStr, dateFormat);
            return null;
        }
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

    /**
     * Get first day of given year and month
     *
     * @param year  eg: 2020
     * @param month eg: 2
     * @return first day of given year and month
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        if (year < 0 || month < 0) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        return cal.getTime();
    }

    public static Date getFirstDayOfMonth(Date now) {
        if (now == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getLastDayOfMonth(Date now) {
        if (now == null) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * Get last day of given year and month
     *
     * @param year  eg: 2020
     * @param month eg: 2
     * @return last day of given year and month
     */
    public static Date getLastDayOfMonth(int year, int month) {
        if (year < 0 || month < 0) {
            return null;
        }

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        return new Date(cal.getTimeInMillis() - 1);
    }

}
