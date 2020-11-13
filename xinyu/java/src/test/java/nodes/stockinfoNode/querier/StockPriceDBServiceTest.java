package nodes.stockinfoNode.querier;

import com.google.inject.Key;
import nodes.NodeModule;
import nodes.stockinfoNode.crawler.AlphavantageCrawler;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.utils.Converter;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * Created by Xinyu Zhu on 2020/11/13, 0:30
 * nodes.stockinfoNode.querier in codingDimensionTemplate
 */
public class StockPriceDBServiceTest {
    StockPriceDBService stockPriceDBService = NodeModule.getGlobalInjector().getInstance(StockPriceDBService.class);
    AlphavantageCrawler<StockCompanyPOJO> crawler = NodeModule.getGlobalInjector().getInstance(new Key<>(){});


    @Test
    public void insertCompany() throws Exception {
        crawler.addSymbolToQueue("IBM");
        Future<Optional<StockCompanyPOJO>> result = crawler.getResultFuture("IBM");
        StockCompanyPOJO stockDailyRecordList = result.get().get();
        System.out.println(stockDailyRecordList);


        stockPriceDBService.insertCompany(stockDailyRecordList);

        crawler.shutDown();
    }

}