package nodes.personalAccountingNode.db;

import nodes.NodeModule;
import org.junit.Test;

/**
 * Created by Xinyu Zhu on 2020/11/18, 23:59
 * nodes.personalAccountingNode.db in codingDimensionTemplate
 */
public class StockTransactionUpdaterTest {

    StockTransactionUpdater stockTransactionUpdater = NodeModule.getGlobalInjector().getInstance(StockTransactionUpdater.class);

    @Test
    public void autoUpdate() {
        stockTransactionUpdater.autoUpdate();
    }
}