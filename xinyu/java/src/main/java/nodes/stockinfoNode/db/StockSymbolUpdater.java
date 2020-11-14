package nodes.stockinfoNode.db;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/13, 3:03
 * nodes.stockinfoNode in codingDimensionTemplate
 */
public interface StockSymbolUpdater {
    // Try to update with the result from stockSymbolCrawler or a pre defined list
    void update();

    // This is for whitelisted symbol
    void update(List<String> symbols);
}
