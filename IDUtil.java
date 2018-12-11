package com.linghong.molian.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 用来生成13位主键
 *
 * @author luck_nhb
 */
public class IDUtil {

    /**
     * 订单id
     *
     * @return
     */
    public static long getOrderId() {
        int r1 = (int) (Math.random() * (9) + 11);//产生2个0-9的随机数
        int r2 = (int) (Math.random() * (10)+11);
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = date.format(new Date());
        return Long.parseLong(format + r1 + r2);
    }

    /**
     * 随机生成id
     *
     * @return
     */
    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 生成随机码
     *
     * @return
     */
    public static String getCode() {
        return getId();
    }

    /**
     * @return String UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * @param number int
     * @return String[] UUID
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }


    public static void main(String[] nhb) {
        System.out.println(getOrderId());
    }


}
