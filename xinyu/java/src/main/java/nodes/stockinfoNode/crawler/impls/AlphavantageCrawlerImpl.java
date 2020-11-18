package nodes.stockinfoNode.crawler.impls;

import com.google.inject.Inject;
import com.google.inject.Key;
import nodes.NodeModule;
import nodes.crawlerNode.AutoCoolDownCrawler;
import nodes.crawlerNode.constants.CrawlerConstant;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.crawler.constants.WebsiteConstant;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.*;
import java.util.concurrent.Future;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:53
 * nodes.stockinfoNode.crawler.impls in codingDimensionTemplate
 */
public class AlphavantageCrawlerImpl implements AlphavantageCrawler<List<StockDailyRecordPOJO>> {

    private final Set<String> acceptedSymbol;
    private final AutoCoolDownCrawler<List<StockDailyRecordPOJO>> crawler;

    @Inject
    public AlphavantageCrawlerImpl(AutoCoolDownCrawler<List<StockDailyRecordPOJO>> crawler) {
        this.crawler = crawler;
        acceptedSymbol = new HashSet<>();
    }

    // Build param for query online
    private static Map<String, String> buildRequestParamForDailyPrice(String symbol) {
        Map<String, String> requestparam = new HashMap<>();
        requestparam.put("function", "TIME_SERIES_DAILY_ADJUSTED");
        requestparam.put("symbol", symbol);
        requestparam.put("outputsize", "full");
        requestparam.put("apikey", WebsiteConstant.LOAD_KEY);
        return requestparam;
    }

    public static void main(String[] args) throws Exception {
        AlphavantageCrawler<List<StockDailyRecordPOJO>> alphavantageCrawler = NodeModule.getGlobalInjector().getInstance(new Key<>(){});
        alphavantageCrawler.addSymbolToQueue("IBM");

        Future<Optional<List<StockDailyRecordPOJO>>> result = alphavantageCrawler.getResultFuture("IBM");
        List<StockDailyRecordPOJO> stockDailyRecordList = result.get().get();
        System.out.println(stockDailyRecordList.size());
        System.out.println(stockDailyRecordList.get(0));
        alphavantageCrawler.shutDown();
    }

    @Override
    public synchronized void addSymbolToQueue(String symbol) throws Exception {
        if (!acceptedSymbol.contains(symbol)) {
            crawler.addJobToQueue(symbol, WebsiteConstant.ALPHAVANTAGE_API_ENDPOINT,
                    buildRequestParamForDailyPrice(symbol), CrawlerConstant.DEFAULT_HEADER);
            acceptedSymbol.add(symbol);
        }
    }



    @Override
    public Future<Optional<List<StockDailyRecordPOJO>>> getResultFuture(String symbol) throws Exception {
        if (!acceptedSymbol.contains(symbol)) {
            addSymbolToQueue(symbol);
        }
        List<Future<Optional<List<StockDailyRecordPOJO>>>> result = crawler.getResultFuture(symbol);
        if (!result.isEmpty()) {
            if (result.size() > 1) {
                System.err.println("Error happend, somehow got two result for the same symbol, returned the first one");
            }
            return result.get(0);
        } else {
            // Invalid symbol
            return null;
        }
    }

    @Override
    public void shutDown() {
        this.crawler.shutDown();
    }

}
