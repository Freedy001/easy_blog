package com.freedy.backend.common.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";

    public static String formatTime(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }

    public static Date formatTime(String formattedDate) {
        return format(formattedDate, DATE_TIME_PATTERN);
    }

    public static String formatDate(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static Date formatDate(String formattedDate) {
        return format(formattedDate, DATE_PATTERN);
    }

    private static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    private static Date format(String formattedDate, String pattern) {
        if (StringUtils.hasText(formattedDate)) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            try {
                return df.parse(formattedDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
