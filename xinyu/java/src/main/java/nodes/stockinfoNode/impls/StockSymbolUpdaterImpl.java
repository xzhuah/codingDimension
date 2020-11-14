package nodes.stockinfoNode.impls;

import com.google.inject.Inject;
import nodes.stockinfoNode.constants.StockConstant;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.crawler.StockSymbolCrawler;
import nodes.stockinfoNode.crawler.constants.WebsiteConstant;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.querier.StockInfoDBService;

import java.util.*;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import static nodes.stockinfoNode.crawler.constants.WebsiteConstant.SP_500_SYMBOL;

/**
 * Created by Xinyu Zhu on 2020/11/13, 2:06
 * nodes.stockinfoNode.impls in codingDimensionTemplate
 *
 * Better to select < 500 stocks, since the API has a daily limit
 * Auto skip those invalid symbol
 */
public class StockSymbolUpdaterImpl implements nodes.stockinfoNode.StockSymbolUpdater {
    private final AlphavantageCrawler<StockCompanyPOJO> companyInfoCrawler;
    private final StockSymbolCrawler stockSymbolCrawler;
    private final StockInfoDBService dbService;

    @Inject
    public StockSymbolUpdaterImpl(AlphavantageCrawler<StockCompanyPOJO> companyInfoCrawler, StockSymbolCrawler stockSymbolCrawler, StockInfoDBService dbService) {
        this.companyInfoCrawler = companyInfoCrawler;
        this.stockSymbolCrawler = stockSymbolCrawler;
        this.dbService = dbService;
    }

    // Try to update with the result from stockSymbolCrawler or a pre defined list
    @Override
    public void update() {
        try {
            if (SP_500_SYMBOL.isEmpty()) {
                System.out.println("Using All Symbols to update");
                update(stockSymbolCrawler.getAllStockSymbols());
            } else {
                System.out.println("Using SP 500 Symbols to update");
                update(SP_500_SYMBOL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Update failed since can't get all stock symbols");
        }
    }

    private Set<String> getAllSymbolInDatabase() {
        Set<String> allSymbol = new HashSet<>();
        dbService.getCompanyInfoCollection().find().forEach((Consumer<? super StockCompanyPOJO>) record ->
            allSymbol.add(record.getSymbol()));
        return allSymbol;
    }

    // This is for whitelisted symbol
    @Override
    public void update(List<String> symbols) {
        // Preprocess
        symbols = preProcessSymbol(symbols);
        // Deduplication
        Set<String> deduplicatedSymbol = new HashSet<>(symbols);

        symbols = new ArrayList<>(deduplicatedSymbol.size());

        Set<String> symbolAlreadyHave = getAllSymbolInDatabase();
        // remove those already have
        if (!StockConstant.OVERRIDE_WHEN_UPDATE) {
            deduplicatedSymbol.stream().filter(symbol -> !symbolAlreadyHave.contains(symbol)).forEach(symbols::add);
        } else {
            symbols.addAll(deduplicatedSymbol);
        }

        // convert to set, used for future deduplication
        deduplicatedSymbol = new HashSet<>(symbols);

        companyInfoCrawler.addSymbolsToQueue(symbols);

        List<StockCompanyPOJO> validResult = new ArrayList<>(symbols.size());

        for(String symbol : symbols) {
            boolean firstProcessSuccess = false;
            try {
                Future<Optional<StockCompanyPOJO>> resultFuture = companyInfoCrawler.getResultFuture(symbol);
                // Sleep some time
                if (symbols.size() > 5) {
                    Thread.sleep(WebsiteConstant.COOL_DOWN_TIME);
                }

                Optional<StockCompanyPOJO> optionalResult = resultFuture.get();
                if (optionalResult.isPresent()) {
                    System.out.println("Successfully got data for " + symbol);
                    firstProcessSuccess = true;
                    validResult.add(optionalResult.get());
                    dbService.insertCompany(optionalResult.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!firstProcessSuccess) {
                // remove the last character of the symbol and try again
                String newSymbol = removeLastCharacter(symbol);
                if (deduplicatedSymbol.contains(newSymbol)) {
                    continue;
                } else if (!StockConstant.OVERRIDE_WHEN_UPDATE && symbolAlreadyHave.contains(newSymbol)){
                    continue;
                } else {
                    deduplicatedSymbol.add(newSymbol);
                }
                try {
                    companyInfoCrawler.addSymbolToQueue(newSymbol);
                    Future<Optional<StockCompanyPOJO>> resultFuture = companyInfoCrawler.getResultFuture(symbol);
                    // Sleep some time
                    if (symbols.size() > 5) {
                        Thread.sleep(WebsiteConstant.COOL_DOWN_TIME);
                    }
                    Optional<StockCompanyPOJO> optionalResult = resultFuture.get();
                    optionalResult.ifPresent(result -> {
                        System.out.println("Successfully got data for " + newSymbol + " at second try");
                        validResult.add(result);
                        dbService.insertCompany(result);
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Print out what new symbol is added

        List<String> symbolAdded = new ArrayList<>(validResult.size());
        validResult.forEach(result -> {
            symbolAdded.add(result.getSymbol());
        });
        System.out.println("These symbol are successfully added:" + symbolAdded);



    }

    private List<String> preProcessSymbol(List<String> symbols) {
        List<String> processedSymbols = new ArrayList<>(symbols.size());
        symbols.forEach( symbol -> {
            symbol = preProcessSymbol(symbol);
            if (symbol.length() >= 1) {
                processedSymbols.add(symbol);
            }
        });
        return processedSymbols;
    }

    private String preProcessSymbol(String symbol) {
        // remove any invalid characters
        return symbol.replaceAll("[^a-zA-Z]", "");
    }

    private String removeLastCharacter(String symbol) {
        if (symbol.length() <= 1) {
            return "";
        }
        return symbol.substring(0, symbol.length() - 1);
    }
}
