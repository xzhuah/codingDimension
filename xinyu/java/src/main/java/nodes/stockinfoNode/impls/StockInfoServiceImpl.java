package nodes.stockinfoNode.impls;

import com.google.inject.Inject;
import com.mongodb.Block;
import com.mongodb.client.model.Sorts;
import common.framework.BaseModelProvider;
import common.time.TimeInterval;
import nodes.NodeModelProvider;
import nodes.stockinfoNode.StockInfoModelProvider;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.db.PriceAutoUpdater;
import nodes.stockinfoNode.db.StockInfoDBService;
import nodes.stockinfoNode.db.StockSymbolUpdater;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Filters.*;
import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:51
 * nodes.stockinfoNode.impls in codingDimensionTemplate
 */
public class StockInfoServiceImpl implements StockInfoService {

    private final PriceAutoUpdater priceAutoUpdater;
    private final StockSymbolUpdater symbolUpdater;
    private final StockInfoDBService stockInfoDBService;

    private final Set<String> outOfDateSymbol;
    private boolean autoUpdate;

    @Inject
    private StockInfoServiceImpl(PriceAutoUpdater priceAutoUpdater, StockSymbolUpdater symbolUpdater, StockInfoDBService stockInfoDBService) {
        this.priceAutoUpdater = priceAutoUpdater;
        this.symbolUpdater = symbolUpdater;
        this.stockInfoDBService = stockInfoDBService;

        outOfDateSymbol = new HashSet<>();
        autoUpdate = false;
        priceAutoUpdater.getOutOfDateCompany().forEach(companyPOJO -> outOfDateSymbol.add(companyPOJO.getSymbol()));
    }

    @Override
    public StockInfoModelProvider getSortedPriceForSymbol(String symbol, TimeInterval timeInterval) {
        ensureUpdated(symbol);
        return BaseModelProvider.toModelProvider(queryPrice(and(eq("symbol", symbol),
                gte("time", timeInterval.getStartTimeInMillis()),
                lt("time", timeInterval.getEndTimeInMillis())), Sorts.ascending("time")),
                StockDailyRecordPOJO.class, StockInfoModelProvider.class);

    }

    @Override
    public List<String> filterSymbols(Collection<String> symbols) {
        ensureUpdated(symbols);
        List<String> filteredSymbol = new ArrayList<>(symbols.size());
        symbols.forEach(symbol -> {
            if (symbolUpdater.isExistingSymbol(symbol)) {
                filteredSymbol.add(symbol);
            }
        });
        return filteredSymbol;
    }

    @Override
    public StockInfoModelProvider sortCompanyByMarket(Collection<String> symbols) {
        ensureUpdated(symbols);
        return BaseModelProvider.toModelProvider(queryCompanies(in("symbol", symbols), Sorts.descending("market")),
                StockCompanyPOJO.class, StockInfoModelProvider.class);
    }

    @Override
    public StockInfoModelProvider sortCompanyByEmployee(Collection<String> symbols) {
        ensureUpdated(symbols);
        return BaseModelProvider.toModelProvider(queryCompanies(in("symbol", symbols), Sorts.descending("employee")),
                StockCompanyPOJO.class, StockInfoModelProvider.class);
    }


    @Override
    public NodeModelProvider sortCompanyByMarket() {
        List<StockCompanyPOJO> result = queryCompanies(null, Sorts.descending("market"));
        NodeModelProvider provider = new NodeModelProvider();
        provider.setAllModel(result, StockCompanyPOJO.class);
        return provider;
    }

    @Override
    public StockInfoModelProvider sortCompanyByEmployee() {
        return BaseModelProvider.toModelProvider(queryCompanies(null, Sorts.descending("employee")),
                StockCompanyPOJO.class, StockInfoModelProvider.class);
    }

    @Override
    public List<String> getAllSymbols() {
        List<String> allSymbols = new ArrayList<>();
        queryCompanies(null, null).forEach(companyPOJO -> allSymbols.add(companyPOJO.getSymbol()));
        return allSymbols;
    }

    @Override
    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
    }


    private List<StockCompanyPOJO> queryCompanies(Bson filterRule, Bson sortingRule) {
        List<StockCompanyPOJO> sortedResult = new ArrayList<>();
        Block<StockCompanyPOJO> insertCompanyBlock = sortedResult::add;
        if (null != filterRule && null != sortingRule) {
            stockInfoDBService.getCompanyInfoCollection().find(filterRule).sort(sortingRule).forEach(insertCompanyBlock);
        } else if (null != filterRule) {
            stockInfoDBService.getCompanyInfoCollection().find(filterRule).forEach(insertCompanyBlock);
        } else if (null != sortingRule) {
            stockInfoDBService.getCompanyInfoCollection().find().sort(sortingRule).forEach(insertCompanyBlock);
        } else {
            // by default it is sorted according to primary key
            stockInfoDBService.getCompanyInfoCollection().find().forEach(insertCompanyBlock);
        }
        return sortedResult;
    }

    private List<StockDailyRecordPOJO> queryPrice(Bson filterRule, Bson sortingRule) {
        List<StockDailyRecordPOJO> sortedResult = new ArrayList<>();
        Block<StockDailyRecordPOJO> insertPriceBlock = sortedResult::add;
        if (null != filterRule && null != sortingRule) {
            stockInfoDBService.getPriceCollection().find(filterRule).sort(sortingRule).forEach(insertPriceBlock);
        } else if (null != filterRule) {
            stockInfoDBService.getPriceCollection().find(filterRule).forEach(insertPriceBlock);
        } else if (null != sortingRule) {
            stockInfoDBService.getPriceCollection().find().sort(sortingRule).forEach(insertPriceBlock);
        } else {
            // by default it is sorted according to primary key
            stockInfoDBService.getPriceCollection().find().forEach(insertPriceBlock);
        }
        return sortedResult;
    }

    // This should be run every amount of time to ensure the list is updated
    private void updateOutOfDateCompany() {
        outOfDateSymbol.clear();
        priceAutoUpdater.getOutOfDateCompany().forEach(companyPOJO -> outOfDateSymbol.add(companyPOJO.getSymbol()));
    }

    private boolean isOutOfDate(String symbol) {
        return outOfDateSymbol.contains(symbol);
    }

    private void ensureUpdated(String symbol) {
        if (autoUpdate) {
            if (symbolUpdater.isExistingSymbol(symbol)) {
                if (isOutOfDate(symbol)) {
                    priceAutoUpdater.update(symbol);
                    outOfDateSymbol.remove(symbol);
                }
            } else {
                checkStatus(!outOfDateSymbol.contains(symbol), "Data inconsistant! outOfDateSymbol contains symbol that does not exist:" + symbol);
                symbolUpdater.update(symbol);
                priceAutoUpdater.update(symbol);
            }
        }
        // Errors may happen during the updating process, but we consider it has an invalid symbol if error happens
        // return empty result for in valid symbol instead of keep trying to update it.
    }

    private void ensureUpdated(Collection<String> symbols) {
        if (autoUpdate) {
            symbols = new HashSet<>(symbols);
            for (String symbol : symbols) {
                if (symbolUpdater.isExistingSymbol(symbol)) {
                    if (isOutOfDate(symbol)) {
                        priceAutoUpdater.update(symbol);
                        outOfDateSymbol.remove(symbol);
                    }
                } else {
                    checkStatus(!outOfDateSymbol.contains(symbol), "Data inconsistant! outOfDateSymbol contains symbol that does not exist:" + symbol);
                    symbolUpdater.update(symbol);
                    priceAutoUpdater.update(symbol);
                }
            }
        }
    }


}
