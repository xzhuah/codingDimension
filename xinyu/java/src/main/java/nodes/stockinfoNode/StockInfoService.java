package nodes.stockinfoNode;

import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.Collection;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/11, 20:48
 * nodes.stockinfoNode in codingDimensionTemplate
 *
 * This is the service interface with which you can automatically get the up to date price of any stock
 * It will do update in a on-demand manner to optimize online API usage, when an update is not required, price
 * information will be retrieved from database instead of online API
 */
public interface StockInfoService {

    List<StockDailyRecordPOJO> getSortedPriceForSymbol(String symbol);

    List<StockCompanyPOJO> sortCompanyByMarket(Collection<String> symbols);

    List<StockCompanyPOJO> sortCompanyByEmployee(Collection<String> symbols);

    List<StockCompanyPOJO> sortCompanyByMarket();

    List<StockCompanyPOJO> sortCompanyByEmployee();

    List<String> getAllSymbols();

    void setAutoUpdate(boolean autoUpdate);
}
