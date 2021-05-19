package com.freedy.backend.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Freedy
 * @date 2021/5/5 23:10
 */
public class DateUtils {
    private static final String[] NUMBERS = { "零", "壹", "贰", "叁", "肆", "伍",
            "陆", "柒", "捌", "玖" };
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";

    public static String formatTime(Long time) {
        return format(new Date(time), DATE_TIME_PATTERN);
    }

    public static String formatTime(Date date) {
        return format(date, DATE_TIME_PATTERN);
    }

    public static Date formatTime(String formattedDate) {
        return format(formattedDate, DATE_TIME_PATTERN);
    }

    public static String formatDate(Long time) {
        return format(new Date(time), DATE_PATTERN);
    }

    public static String formatDate(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static Date formatDate(String formattedDate) {
        return format(formattedDate, DATE_PATTERN);
    }

    /**
     * 输出格式为 伍月 17,2021 的时间字符串
     */
    public static String formatChineseDate(Long date){
        String format = format(new Date(date), DATE_PATTERN);
        String[] dataSplit = format.split("-");
        StringBuilder builder = new StringBuilder();
        //格式化月
        char[] month = dataSplit[1].toCharArray();
        if ( month[0]=='0'){
            builder.append(NUMBERS[month[1]-48]).append("月 ");
        }else {
            builder.append("十").append(NUMBERS[month[1]-48]).append("月 ");
        }
        builder.append(dataSplit[2]).append(",").append(dataSplit[0]);
        return builder.toString();
    }

    public static String formatRelevantTime(Long creatTime) {
        long time = new Date().getTime();
        if (time-creatTime<60*60*1000){
            //一小时内
            return (time-creatTime)/60*1000+"分钟前";
        } else if (time-creatTime<60*60*24*1000) {
            //一天内
            return (time-creatTime)/60*60*1000+"小时前";
        } else if (time-creatTime< 60L*60*24*30*1000) {
            //一个月内
            return (time-creatTime)/60*60*24*1000+"天前";
        }else if (time-creatTime< 60L*60*24*30*12*1000) {
            //一年月内
            return (time-creatTime)/60*60*24*30*1000+"月前";
        }else {
            return formatTime(creatTime);
        }
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
