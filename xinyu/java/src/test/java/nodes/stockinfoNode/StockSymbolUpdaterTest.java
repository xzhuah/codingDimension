package nodes.stockinfoNode;

import nodes.NodeModule;
import nodes.stockinfoNode.db.StockSymbolUpdater;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void updateOne() {
        stockSymbolUpdater.update(List.of("IBM"));
    }
}