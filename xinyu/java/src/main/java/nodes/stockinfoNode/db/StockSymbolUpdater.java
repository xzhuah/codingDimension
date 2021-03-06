package nodes.stockinfoNode.db;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/13, 3:03
 * nodes.stockinfoNode in codingDimensionTemplate
 */
public interface StockSymbolUpdater {
    // Try to update with the result from stockSymbolCrawler or a pre defined list
    void update();

    // This is for whitelisted symbol, it will force update all the symbols in list
    void update(List<String> symbols);

    default void update(String symbol) {
        update(List.of(symbol));
    }

    boolean isExistingSymbol(String symbol);
}
