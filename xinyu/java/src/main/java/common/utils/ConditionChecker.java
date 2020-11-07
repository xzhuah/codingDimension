package common.utils;

/**
 * Created by Xinyu Zhu on 2020/11/7, 1:44
 * common.utils in codingDimensionTemplate
 */
public class ConditionChecker {

    public static void checkStatus(boolean status) {
        if (!status) {
            throw new RuntimeException("ConditionChecker detect invalid status");
        }
    }

    public static void checkStatus(boolean status, String msg) {
        if (!status) {
            throw new RuntimeException(msg);
        }
    }

    public static void checkStatus(String errorCode, boolean status) {
        if (!status) {
            throw new RuntimeException(errorCode);
        }
    }

    public static void checkStatus(String errorCode, boolean status, String msg) {
        if (!status) {
            throw new RuntimeException(errorCode + ":" + msg);
        }
    }
}
