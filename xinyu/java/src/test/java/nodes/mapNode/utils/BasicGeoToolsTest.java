package nodes.mapNode.utils;

import nodes.mapNode.models.Position;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Xinyu Zhu on 2020/11/29, 23:27
 * nodes.mapNode.utils in codingDimensionTemplate
 */
public class BasicGeoToolsTest {

    @Test
    public void test() {
        Position center = new Position(-1.1, 1.1);

        double distance = BasicGeoTools.getDistance(BasicGeoTools.moveToEast(center, 20000)
                ,BasicGeoTools.moveToWest(center, 30000));
        assertTrue(Math.abs(distance - (20000 + 30000)) < 1);
    }

}