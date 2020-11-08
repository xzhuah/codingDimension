package common.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Xinyu Zhu on 2020/11/3, 22:02
 * common.time in AllInOne
 */
public class TimeClientTest {

    @Test
    public void getCurrentTimeString() {
        System.out.println(TimeClient.getCurrentTimeString(TimeConstant.dateOnlyFormat));
        assertEquals(TimeClient.getCurrentTimeString(), TimeClient.getCurrentTimeString(TimeConstant.dateTimeFormat));
    }
}