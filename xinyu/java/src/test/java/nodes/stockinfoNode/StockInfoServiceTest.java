package nodes.stockinfoNode;

import nodes.NodeModelProvider;
import nodes.NodeModule;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

/**
 * Created by Xinyu Zhu on 2020/11/14, 18:14
 * nodes.stockinfoNode in codingDimensionTemplate
 */
public class StockInfoServiceTest {

    StockInfoService stockInfoService = NodeModule.getGlobalInjector().getInstance(StockInfoService.class);

    @Test
    public void getSortedPriceForSymbol() {
        List<StockDailyRecordPOJO> sortedPrice = stockInfoService.getSortedPriceForSymbol("IBM").getAllModel(StockDailyRecordPOJO.class).get();
        System.out.println(sortedPrice);
        System.out.println(sortedPrice.size());
    }

    @Test
    public void sortCompanyByMarket() {
        NodeModelProvider sortedCompany = stockInfoService.sortCompanyByMarket();
        Optional<List<StockCompanyPOJO>> result = sortedCompany.getAllModel(StockCompanyPOJO.class);
        assert result.isPresent();
        System.out.println(result.get());
        System.out.println(result.get().size());
    }
    

    @Test
    public void getAllSymbols() {
        List<String> symbols = stockInfoService.getAllSymbols();
        System.out.println(symbols);
        System.out.println(symbols.size());
    }
}