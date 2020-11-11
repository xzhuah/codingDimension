package nodes.stockinfoNode.crawler.impls;

import nodes.crawlerNode.BaseCrawler;
import nodes.crawlerNode.constants.CrawlerConstant;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.crawler.constants.WebsiteConstant;
import nodes.stockinfoNode.crawler.facade.CompanyInfoProcessor;
import nodes.stockinfoNode.models.StockCompanyPOJO;

import java.util.*;
import java.util.concurrent.Future;

public class AlphavantageSymbolCrawlerImpl implements AlphavantageCrawler<StockCompanyPOJO> {

    private final Set<String> acceptedSymbol;
    private final BaseCrawler<StockCompanyPOJO> crawler;


    public AlphavantageSymbolCrawlerImpl() {
        acceptedSymbol = new HashSet<>();
        crawler = new BaseCrawler<>(CompanyInfoProcessor.getInstance());
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
        AlphavantageCrawler<StockCompanyPOJO> alphavantageCrawler = new AlphavantageSymbolCrawlerImpl();
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
    public void addSymbolsToQueue(Collection<String> symbols) {
        for (String symbol : symbols) {
            try {
                this.addSymbolToQueue(symbol);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Skipped " + symbol);
            }
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