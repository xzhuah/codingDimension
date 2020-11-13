package nodes.stockinfoNode.crawler.impls;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import nodes.crawlerNode.BaseCrawler;
import nodes.stockinfoNode.crawler.constants.WebsiteConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by Xinyu Zhu on 2020/11/13, 1:30
 * nodes.stockinfoNode.crawler.impls in codingDimensionTemplate
 */
public class StockSymbolCrawlerImpl implements nodes.stockinfoNode.crawler.StockSymbolCrawler {
    private final BaseCrawler<JsonArray> crawler;

    @Inject
    public StockSymbolCrawlerImpl(BaseCrawler<JsonArray> crawler) {
        this.crawler = crawler;
    }

    private JsonArray getAllRecord() throws Exception {
        crawler.addJobToQueue(WebsiteConstant.SYMBOL_QUERY_ENDPOINT);
        List<Future<Optional<JsonArray>>> result = crawler.getResultFuture(WebsiteConstant.SYMBOL_QUERY_ENDPOINT);
        if (result.isEmpty()) {
            return null;
        }
        Optional<JsonArray> jsonArray = result.get(0).get();
        if (!jsonArray.isPresent()) {
            return null;
        }
        JsonArray allSymbol = jsonArray.get();
        return allSymbol;
    }

    @Override
    public List<String> getAllStockSymbols() throws Exception {

        JsonArray allSymbol = getAllRecord();

        if (null == allSymbol || allSymbol.size() == 0) {
            return new ArrayList<>();
        }
        List<String> allStockSymbol = new ArrayList<>(allSymbol.size());
        allSymbol.forEach(symbolRecord -> {
            try {
                JsonObject record = symbolRecord.getAsJsonObject();
                if (!record.get("type").getAsString().equals("crypto")) {
                    allStockSymbol.add(record.get("symbol").getAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

       return allStockSymbol;

    }

    @Override
    public List<String> getAllCryptoSymbols() throws Exception {
        JsonArray allSymbol = getAllRecord();

        if (null == allSymbol || allSymbol.size() == 0) {
            return new ArrayList<>();
        }
        List<String> allCryptoSymbol = new ArrayList<>(allSymbol.size());
        allSymbol.forEach(symbolRecord -> {
            try {
                JsonObject record = symbolRecord.getAsJsonObject();
                if (record.get("type").getAsString().equals("crypto")) {
                    allCryptoSymbol.add(record.get("symbol").getAsString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return allCryptoSymbol;
    }
}
