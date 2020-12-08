package nodes.stockinfoNode;

import common.time.TimeInterval;
import nodes.NodeModelProvider;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.Collection;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/11, 20:48
 * nodes.stockinfoNode in codingDimensionTemplate
 * <p>
 * This is the service interface with which you can automatically get the up to date price of any stock
 * It will do update in a on-demand manner to optimize online API usage, when an update is not required, price
 * information will be retrieved from database instead of online API
 */
public interface StockInfoService {

    StockInfoModelProvider getSortedPriceForSymbol(String symbol, TimeInterval timeInterval);

    default StockInfoModelProvider getSortedPriceForSymbol(String symbol) {
        return getSortedPriceForSymbol(symbol, TimeInterval.getUpToNowInterval());
    }

    List<String> filterSymbols(Collection<String> symbols);

    StockInfoModelProvider sortCompanyByMarket(Collection<String> symbols);

    StockInfoModelProvider sortCompanyByEmployee(Collection<String> symbols);

    NodeModelProvider sortCompanyByMarket();

    StockInfoModelProvider sortCompanyByEmployee();

    List<String> getAllSymbols();

    void setAutoUpdate(boolean autoUpdate);
}
