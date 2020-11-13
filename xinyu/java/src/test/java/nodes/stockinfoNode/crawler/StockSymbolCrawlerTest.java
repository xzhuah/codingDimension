package nodes.stockinfoNode.crawler;

import nodes.NodeModule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Xinyu Zhu on 2020/11/13, 1:53
 * nodes.stockinfoNode.crawler in codingDimensionTemplate
 */
public class StockSymbolCrawlerTest {

    StockSymbolCrawler crawler = NodeModule.getGlobalInjector().getInstance(StockSymbolCrawler.class);

    @Test
    public void getAllStockSymbols() throws Exception {
        List<String> result = crawler.getAllStockSymbols();
        System.out.println(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getAllCryptoSymbols() throws Exception {
        List<String> result = crawler.getAllCryptoSymbols();
        System.out.println(result);
        assertFalse(result.isEmpty());
    }
}