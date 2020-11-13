package nodes.stockinfoNode;

import nodes.NodeModule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Xinyu Zhu on 2020/11/13, 3:04
 * nodes.stockinfoNode in codingDimensionTemplate
 */
public class StockSymbolUpdaterTest {

    StockSymbolUpdater stockSymbolUpdater = NodeModule.getGlobalInjector().getInstance(StockSymbolUpdater.class);

    @Test
    public void update() {
        stockSymbolUpdater.update();
    }
}