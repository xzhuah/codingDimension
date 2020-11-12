package nodes.stockinfoNode.crawler.impls;

import com.google.inject.Inject;
import com.google.inject.Key;
import nodes.NodeModule;
import nodes.crawlerNode.BaseCrawler;
import nodes.crawlerNode.constants.CrawlerConstant;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.crawler.constants.WebsiteConstant;
import nodes.stockinfoNode.models.StockCompanyPOJO;

import java.util.*;
import java.util.concurrent.Future;

public class AlphavantageSymbolCrawlerImpl implements AlphavantageCrawler<StockCompanyPOJO> {

    private final Set<String> acceptedSymbol;
    private final BaseCrawler<StockCompanyPOJO> crawler;

    @Inject
    public AlphavantageSymbolCrawlerImpl(BaseCrawler<StockCompanyPOJO> crawler) {
        this.crawler = crawler;
        acceptedSymbol = new HashSet<>();
    }

    // Build param for query online
    private static Map<String, String> buildRequestParamForDailyPrice(String symbol) {
        Map<String, String> requestparam = new HashMap<>();
        requestparam.put("function", "OVERVIEW");
        requestparam.put("symbol", symbol);
        requestparam.put("apikey", WebsiteConstant.loadKey);
        return requestparam;
    }

    public static void main(String[] args) throws Exception {
        AlphavantageCrawler<StockCompanyPOJO> alphavantageCrawler = NodeModule.getGlobalInjector().getInstance(new Key<>(){});
        alphavantageCrawler.addSymbolToQueue("IBM");

        Future<Optional<StockCompanyPOJO>> result = alphavantageCrawler.getResultFuture("IBM");
        StockCompanyPOJO stockDailyRecordList = result.get().get();
        System.out.println(stockDailyRecordList);
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
    public Future<Optional<StockCompanyPOJO>> getResultFuture(String symbol) throws Exception {
        if (!acceptedSymbol.contains(symbol)) {
            addSymbolToQueue(symbol);
        }
        List<Future<Optional<StockCompanyPOJO>>> result = crawler.getResultFuture(symbol);
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