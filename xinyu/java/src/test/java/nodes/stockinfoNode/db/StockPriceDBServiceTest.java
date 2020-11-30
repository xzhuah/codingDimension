package nodes.stockinfoNode.db;

import com.google.inject.Key;
import nodes.NodeModule;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by Xinyu Zhu on 2020/11/13, 0:30
 * nodes.stockinfoNode.querier in codingDimensionTemplate
 */
public class StockPriceDBServiceTest {
    StockInfoDBService stockInfoDBService = NodeModule.getGlobalInjector().getInstance(StockInfoDBService.class);
    AlphavantageCrawler<StockCompanyPOJO> crawler = NodeModule.getGlobalInjector().getInstance(new Key<>() {
    });


    @Test
    public void insertCompany() throws Exception {
        crawler.addSymbolToQueue("IBM");
        Future<Optional<StockCompanyPOJO>> result = crawler.getResultFuture("IBM");
        StockCompanyPOJO stockDailyRecordList = result.get().get();
        System.out.println(stockDailyRecordList);


        stockInfoDBService.insertCompany(stockDailyRecordList);

        crawler.shutDown();
    }

}