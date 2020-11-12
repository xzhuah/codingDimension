package nodes.stockinfoNode.impls;

import com.google.inject.Inject;
import nodes.stockinfoNode.PriceAutoUpdater;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import nodes.stockinfoNode.querier.StockPriceDBService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
        // get the current timestamp, -1 day if it is sunday

        // pivot timestamp = current timestamp - 24 * 3600,000 * delta

        // query for that company's record with  timestamp: current timestamp > timestamp > pivot timestamp (some dirty record may have wrong timestamp and this can help filter some error)

        // if exist --> false

        // else true
        return false;
    }

    @Override
    public List<StockCompanyPOJO> getOutOfDateCompany() {
        // get the current timestamp, -1 day if it is sunday

        // pivot timestamp = current timestamp - 24 * 3600,000 * delta

        // query for all company record with timestamp: has current timestamp > timestamp > pivot timestamp

        // create a set of them, set based on primary key (Symbol) only

        return null;
    }
}