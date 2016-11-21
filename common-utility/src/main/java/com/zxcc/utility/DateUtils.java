package com.zxcc.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * Created by xuanzh.cc on 2016/9/8.
 */
public class DateUtils {

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /** 一分钟的秒数 */
    public static final int MINUTE_SECOND = 60;
    /** 一小时的秒数 */
    public static final int HOUR_SECOND = 3600;
    /** 一天的秒数 */
    public static final int DAY_SECOND = 86400;

    /** 一秒钟的毫秒数 */
    public static final int SECOND_MILLISECOND = 1000;
    /** 一分钟的毫秒数 */
    public static final int MINUTE_MILLISECOND = 60000;
    /** 一小时的毫秒数 */
    public static final int HOUR_MILLISECOND = 3600000;
    /** 一天的毫秒数 */
    public static final int DAY_MILLISECOND = 86400000;

    /** 日期格式  yyyy-MM-dd */
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /** 日期时间格式  yyyy-MM-dd HH:mm:ss */
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);

    /**
     * 获得当前的毫秒数
     * @return
     */
    public static long currentTimeMillis(){
        return System.currentTimeMillis();
    }

    /**
     * 获取当前的日期
     * @return
     */
    public static Date now(){
        return new Date();
    }

    /**
     * 获得当前的时间戳
     * @return
     */
    public static int currentTimestamp(){
        return (int) (currentTimeMillis() / SECOND_MILLISECOND);
    }

    /**
     * 格式化日期为 yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String formatTime(Date date){
        return simpleDateFormat.format(date);
    }

    /**
     * 把日期字符串解析为时间,格式化失败则返回当前时间
     * @param timeStr
     * @return
     */
    public static Date parseDate(String timeStr) {
        try {
            return simpleDateFormat.parse(timeStr);
        } catch (ParseException e) {
            logger.error("解析时间错误，时间字符串为：{}", timeStr, e);
        }

        return now();
    }
}
