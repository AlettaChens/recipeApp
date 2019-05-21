package com.example.graduate.findingcooking.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Cw
 * @date 2017/4/12
 */
public class DateFormatUtil {

    public static String transMonth(String date) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MM月");
        try {
            str = sdf.format(Long.parseLong(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String transDay(String date) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        try {
            str = sdf.format(Long.parseLong(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String transTime(String date) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            str = sdf.format(Long.parseLong(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String parseTime(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date time = sdf.parse(data);
            return time.getTime() + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
