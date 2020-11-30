package common.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Xinyu Zhu on 2020/11/3, 21:49
 * common.time in AllInOne
 * UTC time only, timestamp are in milliseconds
 */
public class TimeClient {

    // Apply on timestamp
    public static Date timestampToDate(long timestamp) {
        checkNormalMilliTimestamp(timestamp);
        return new Date(timestamp);
    }

    public static String timestampToString(long timestamp) {
        checkNormalMilliTimestamp(timestamp);
        return timestampToString(timestamp, TimeConstant.dateTimeFormat);
    }

    public static String timestampToString(long timestamp, SimpleDateFormat format) {
        checkNormalMilliTimestamp(timestamp);
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
        checkNormalMilliTimestamp(timestamp);
        return stringToTimestamp(timestampToString(timestamp).split(" ")[0], TimeConstant.dateOnlyFormat);
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Calendar toCalendar(long timestamp) {
        checkNormalMilliTimestamp(timestamp);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return cal;
    }

    // 0 sunday, 1 Monday, ... 6 Saturday
    public static int getWeekday(Date date) {
        Calendar cal = toCalendar(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getWeekday(long timestamp) {
        checkNormalMilliTimestamp(timestamp);
        Calendar cal = toCalendar(timestamp);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    // JANUARY is 1, Feb is 2,...
    public static int getMonth(Date date) {
        Calendar cal = toCalendar(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getMonth(long timestamp) {
        checkNormalMilliTimestamp(timestamp);
        Calendar cal = toCalendar(timestamp);
        return cal.get(Calendar.MONTH) + 1;
    }

    private static void checkNormalMilliTimestamp(long timestamp) {
        // Normally we wont see any MilliTimestamp smaller than this one
        if (timestamp < 5000000000L) {
            System.err.println("TimeClient get a timestamp that might be in second instead of milliseconds, please check if that is an error! Your Timestamp = " + timestamp);
        }
    }

    public static void main(String[] args) throws ParseException {
        //System.out.println(getCurrentTimeString());
        System.out.println(getMonth(System.currentTimeMillis() + 3600000 * 24 * 4));
    }
}
