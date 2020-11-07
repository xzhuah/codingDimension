package nodes.stockinfoNode.crawler.impls;

import common.io.file.PlaintextClient;
import common.io.web.PoolingAsyncHttpClient;
import common.io.web.impl.PoolingAsyncHttpClientImpl;
import common.io.web.models.ResponseProcessResult;
import common.io.web.utils.RequestBuilder;
import nodes.crawlerNode.BaseCrawler;
import nodes.crawlerNode.constants.CrawlerConstant;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.crawler.constants.WebsiteConstant;
import nodes.crawlerNode.constants.CrawlerStatus;
import nodes.stockinfoNode.crawler.facade.DailyPriceProcessor;
import nodes.stockinfoNode.models.StockDailyRecordList;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import org.apache.http.client.methods.HttpGet;

import java.util.*;
import java.util.concurrent.Future;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:53
 * nodes.stockinfoNode.crawler.impls in codingDimensionTemplate
 */
public class AlphavantageCrawlerImpl implements nodes.stockinfoNode.crawler.AlphavantageCrawler {
    private static final String keyFile = "stockinfoKey.pass";
    private static final String apiKey = getKeyForAlphavantageCrawler();

    private Set<String> acceptedSymbol;
    private BaseCrawler crawler;


    public AlphavantageCrawlerImpl() {
        acceptedSymbol = new HashSet<>();
        crawler = new BaseCrawler(DailyPriceProcessor.getInstance());
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
    public Future<ResponseProcessResult> getResultFuture(String symbol) throws Exception {
        if (!acceptedSymbol.contains(symbol)) {
            addSymbolToQueue(symbol);
        }
        List<Future<ResponseProcessResult>> result = crawler.getResultFuture(symbol);
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

    // Build param for query online
    private static Map<String, String> buildRequestParamForDailyPrice(String symbol) {
        Map<String, String> requestparam = new HashMap<>();
        requestparam.put("function", "TIME_SERIES_DAILY_ADJUSTED");
        requestparam.put("symbol", symbol);
        requestparam.put("outputsize", "full");
        requestparam.put("apikey", apiKey);
        return requestparam;
    }

    // Load file from a file (avoid sensitive information being upload to public codebase)
    private static String getKeyForAlphavantageCrawler() {
        String loadKey = PlaintextClient.readFile(keyFile);
        if (null == loadKey) {
            // Default demo key which can be used with a lot of limitation
            System.out.println("Key file not found, use default demo key");
            loadKey = "demo";
        } else {
            loadKey = loadKey.trim();
        }
        return loadKey;
    }

    public static void main(String[] args) throws Exception {
        AlphavantageCrawler alphavantageCrawler = new AlphavantageCrawlerImpl();
        alphavantageCrawler.addSymbolToQueue("IBM");

        Future<ResponseProcessResult> result = alphavantageCrawler.getResultFuture("IBM");
        StockDailyRecordList stockDailyRecordList = (StockDailyRecordList) result.get();
        List<StockDailyRecordPOJO> finalResult = stockDailyRecordList.getStockDailyRecordPOJOList();
        System.out.println(finalResult.size());
        System.out.println(finalResult.get(0));
        alphavantageCrawler.shutDown();
    }

}
