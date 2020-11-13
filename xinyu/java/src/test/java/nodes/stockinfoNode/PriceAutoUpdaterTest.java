package nodes.stockinfoNode;

import nodes.NodeModule;
import org.junit.Test;



/**
 * Created by Xinyu Zhu on 2020/11/11, 21:42
 * nodes.stockinfoNode in codingDimensionTemplate
 */
public class PriceAutoUpdaterTest {

    PriceAutoUpdater autoUpdater;

    @Test
    public void test() {
        autoUpdater = NodeModule.getGlobalInjector().getInstance(PriceAutoUpdater.class);
    }
}