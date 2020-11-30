package common.time;

import lombok.Getter;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/15, 16:27
 * common.time in codingDimensionTemplate
 */
@Getter
public class TimeInterval {
    private long startTimeInMillis = 0;
    private long endTimeInMillis = 1;

    private TimeInterval() {
        this.startTimeInMillis = 0;
        this.endTimeInMillis = System.currentTimeMillis();
    }

    public TimeInterval(long startTimeInMillis, long endTimeInMillis) {
        checkStatus(startTimeInMillis <= endTimeInMillis, String.format("Start time %s is larger than end time %s in TimeInterval", startTimeInMillis, endTimeInMillis));
        this.startTimeInMillis = startTimeInMillis;
        this.endTimeInMillis = endTimeInMillis;
    }

    public static TimeInterval getUpToNowInterval() {
        return new TimeInterval();
    }

    public static TimeInterval getUpToNowInterval(long startTimeInMillis) {
        return new TimeInterval(startTimeInMillis, System.currentTimeMillis());
    }

    public static TimeInterval getUpToNowIntervalWithDuration(long timeInMillis) {
        return new TimeInterval(System.currentTimeMillis() - timeInMillis, System.currentTimeMillis());
    }

}
