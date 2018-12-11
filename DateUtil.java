package com.linghong.dongdong.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换
 *
 * @author luck_nhb
 */
public class DateUtil {
    /**
     * 将Date日期格式转换成"yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @return
     */
    public static String date2SimpleDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }

    /**
     * 将Long型转换成日期格式转换成"yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @return
     */
    public synchronized static String long2SimpleDate(Long date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(date.longValue()));
    }


    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     */
    public synchronized static Long getDistanceDays(Date startTime, Date endTime) {
        long day = 0L;
        long hour = 0L;
        long min = 0L;
        long sec = 0L;
        long time1 = startTime.getTime();
        long time2 = endTime.getTime();
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        diff = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - diff * 24);
        min = ((diff / (60 * 1000)) - diff * 24 * 60 - hour * 60);
        sec = (diff / 1000 - diff * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (sec >= 30){
            min++;
        }
        if (min >= 30){
            hour++;
        }
        if (hour >= 4){
            diff++;
        }
        return diff;
    }


    /**
     * 两个时间相差距离多少小时多少分多少秒
     */
    public synchronized static Long getDistanceHour(Long startTime, Long endTime) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long time1 = startTime;
        long time2 = endTime;
        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24) + day * 24;
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        if (day > 0){
            hour = 24 * day + hour;
        }
        if (min > 30){
            hour = hour + 1L;
        }
        return Long.valueOf(hour+"");
    }
}
