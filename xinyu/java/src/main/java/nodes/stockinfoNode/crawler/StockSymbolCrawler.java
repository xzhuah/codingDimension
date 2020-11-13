package nodes.stockinfoNode.crawler;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/13, 1:51
 * nodes.stockinfoNode.crawler in codingDimensionTemplate
 */
public interface StockSymbolCrawler {
    List<String> getAllStockSymbols() throws Exception;

    List<String> getAllCryptoSymbols() throws Exception;
}
