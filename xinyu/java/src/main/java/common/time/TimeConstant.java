package common.time;

import java.text.SimpleDateFormat;

/**
 * Created by Xinyu Zhu on 2020/11/3, 21:49
 * common.time in AllInOne
 */
public class TimeConstant {
    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateOnlyFormatWithSlash = new SimpleDateFormat("yyyy/MM/dd");
    // Add new format here to use
}
