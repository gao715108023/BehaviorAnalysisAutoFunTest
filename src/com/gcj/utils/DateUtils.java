package com.gcj.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-21
 * Time: 上午11:14
 * To change this template use File | Settings | File Templates.
 */
public class DateUtils {

    private static String timeFormats = "yyyyMMddHHmmss";

    private static String timeFormats1 = "yyyyMMdd";

    private static String timeFormats2 = "yyyy-MM-dd HH:mm:ss";

    private static SimpleDateFormat sdf_s = new SimpleDateFormat(timeFormats);

    private static SimpleDateFormat sdf_s_1 = new SimpleDateFormat(timeFormats1);

    private static SimpleDateFormat sdf_s_2 = new SimpleDateFormat(timeFormats2);

    public static Date getDate(String str) {
        try {
            Date date = sdf_s.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static Date getDate1(String str) {
        try {
            Date date = sdf_s_1.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static Date getDate2(String str) {
        try {
            Date date = sdf_s_2.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static void main(String[] args) {
        String test = "2013-12-04T11:06:44+08:00";
        test = test.substring(0, test.indexOf("+"));
        test = test.replace('T', ' ');
        System.out.println(getDate2(test));
    }
}
