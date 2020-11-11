package common.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Xinyu Zhu on 2020/11/3, 21:49
 * common.time in AllInOne
 * UTC time only
 */
public class TimeClient {

    // Apply on timestamp
    public static Date timestampToDate(long timestamp) {
        return new Date(timestamp);
    }

    public static String timestampToString(long timestamp) {
        return timestampToString(timestamp, TimeConstant.dateTimeFormat);
    }

    public static String timestampToString(long timestamp, SimpleDateFormat format) {
        return dateToString(timestampToDate(timestamp), format);
    }

    // Apply on Date
    public static long dateToTimestamp(Date date) {
        return date.getTime();
    }


    public static String dateToString(Date date) {
        return dateToString(date, TimeConstant.dateTimeFormat);
    }

    public static String dateToString(Date date, SimpleDateFormat format) {
        return format.format(date);
    }


    // Apply on String
    public static long stringToTimestamp(String time) throws ParseException {
        return stringToTimestamp(time, TimeConstant.dateTimeFormat);
    }

    public static long stringToTimestamp(String time, SimpleDateFormat format) throws ParseException {
        return dateToTimestamp(stringtoDate(time, format));
    }

    public static Date stringToDate(String time) throws ParseException {
        return stringtoDate(time, TimeConstant.dateTimeFormat);
    }

    public static Date stringtoDate(String time, SimpleDateFormat format) throws ParseException {
        return format.parse(time);
    }

    // Other handy utils
    public static String getCurrentTimeString() {
        return getCurrentTimeString(TimeConstant.dateTimeFormat);
    }

    public static String getCurrentTimeString(SimpleDateFormat format) {
        return dateToString(timestampToDate(System.currentTimeMillis()), format);
    }

    // 将某个时间戳化为当天的第一秒时间戳
    public static long castToDateTimestamp(long timestamp) throws ParseException {
        return stringToTimestamp(timestampToString(timestamp).split(" ")[0], TimeConstant.dateOnlyFormat);
    }

    public static void main(String[] args) throws ParseException {
        //System.out.println(getCurrentTimeString());
        System.out.println(castToDateTimestamp(1604565331200L));
    }
}
