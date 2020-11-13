package nodes.stockinfoNode.impls;

import com.google.inject.Inject;
import common.time.TimeClient;
import nodes.stockinfoNode.PriceAutoUpdater;
import nodes.stockinfoNode.constants.StockConstant;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import nodes.stockinfoNode.querier.StockPriceDBService;
import nodes.stockinfoNode.utils.Converter;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.nin;
import static common.utils.ConditionChecker.checkStatus;
import static nodes.stockinfoNode.utils.Converter.getTimeFilterForStockDailyRecord;

/**
 * With DeltaDelayPriceAutoUpdaterImpl, you can specify how many days (delta) you are allowed in out of date.
 * Only those company whose latest record in database is delta days before today, will they be considered out of date
 * By default delta = 1 (controlled by StockConstant.DEFAULT_DELTA_IN_DAY_FOR_UPDATE) and weekend will be automatically
 * ignored in calculating delta
 */
public class DeltaDelayPriceAutoUpdaterImpl implements PriceAutoUpdater {

    private final StockPriceDBService stockPriceDBService;
    private final AlphavantageCrawler<List<StockDailyRecordPOJO>> priceInfoCrawler;

    @Inject
    private DeltaDelayPriceAutoUpdaterImpl(StockPriceDBService stockPriceDBService, AlphavantageCrawler<List<StockDailyRecordPOJO>> priceInfoCrawler) {
        this.stockPriceDBService = stockPriceDBService;
        this.priceInfoCrawler = priceInfoCrawler;
    }


    @Override
    public void update(List<StockCompanyPOJO> companies) {

        // Add symbol to queue
        for (StockCompanyPOJO company : companies) {
            try {
                priceInfoCrawler.addSymbolToQueue(company.getSymbol());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(company + ": is skipped during adding to queue due to some error");
            }
        }

        // Fetch future results
        List<Future<Optional<List<StockDailyRecordPOJO>>>> allCompanyResult = new ArrayList<>();
        for (StockCompanyPOJO company : companies) {
            try {
                allCompanyResult.add(priceInfoCrawler.getResultFuture(company.getSymbol()));
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(company + ": is skipped during fetch result due to some error");
            }
        }

        // Process future results
        allCompanyResult.stream().forEach(futureCompanyPriceRecord -> {
            Optional<List<StockDailyRecordPOJO>> companyPriceRecord;
            try {
                companyPriceRecord = futureCompanyPriceRecord.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                companyPriceRecord = Optional.empty();
            } catch (ExecutionException e) {
                e.printStackTrace();
                companyPriceRecord = Optional.empty();
            }
            if (!companyPriceRecord.isPresent()) {
                System.err.println(futureCompanyPriceRecord + ": is skipped during processing due to some error");
                return;
            }
            // Insert to database
            stockPriceDBService.insertPrice(companyPriceRecord.get());
        });
    }

    @Override
    public boolean isOutOfDate(StockCompanyPOJO company) {
        String companySymbol = company.getSymbol();
        checkStatus(null != companySymbol && companySymbol.length() > 0, "Can't process StockCompanyPOJO with no valid primary key: " + company);
        // get the current timestamp, -1 day if it is sunday
        long pivotTimestamp = getOutOfDateTime();
        // query for that company's record with  timestamp: current timestamp > timestamp > pivot timestamp (some dirty record may have wrong timestamp and this can help filter some error)
        Bson timeFilter = getTimeFilterForStockDailyRecord(pivotTimestamp, System.currentTimeMillis());
        Bson primaryKeyFilter = Converter.toPrimaryFilter(company);
        List<StockDailyRecordPOJO> recentRecord = stockPriceDBService.queryPrice(and(timeFilter, primaryKeyFilter));
        // if exist --> false
        return recentRecord.isEmpty();
    }

    @Override
    public List<StockCompanyPOJO> getOutOfDateCompany() {
        long pivotTimestamp = getOutOfDateTime();

        // query for all company record with timestamp: has current timestamp > timestamp > pivot timestamp
        Bson timeFilter = getTimeFilterForStockDailyRecord(pivotTimestamp, System.currentTimeMillis());
        List<StockDailyRecordPOJO> recentRecord = stockPriceDBService.queryPrice(timeFilter);

        // create a set of them, set based on primary key (Symbol) only
        Set<String> upToDateCompanySymbols= new HashSet<>(recentRecord.size());
        recentRecord.forEach(record -> upToDateCompanySymbols.add(record.getSymbol()));

        List<StockCompanyPOJO> outOfDateCompanies = stockPriceDBService.queryCompany(nin("symbol", upToDateCompanySymbols));

        return outOfDateCompanies;
    }

    // Return a timestamp for out of date judgement, and record with time less or equal to pivot time will be considered as out of date
    private long getOutOfDateTime() {
        long currentTimestamp = System.currentTimeMillis();
        long offsetTimestamp = currentTimestamp;
        if (TimeClient.getWeekday(currentTimestamp) == 0) {
            offsetTimestamp -= 3600 * 1000 * 24;
        }
        // pivot timestamp = current timestamp - 24 * 3600,000 * delta
        return offsetTimestamp - 24 * 3600 * 1000 * StockConstant.DEFAULT_DELTA_IN_DAY_FOR_UPDATE;
    }
}