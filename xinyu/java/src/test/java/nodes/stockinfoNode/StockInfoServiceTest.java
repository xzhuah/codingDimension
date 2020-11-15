package nodes.stockinfoNode;

import nodes.NodeModule;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Xinyu Zhu on 2020/11/14, 18:14
 * nodes.stockinfoNode in codingDimensionTemplate
 */
public class StockInfoServiceTest {

    StockInfoService stockInfoService = NodeModule.getGlobalInjector().getInstance(StockInfoService.class);

    @Test
    public void getSortedPriceForSymbol() {
        List<StockDailyRecordPOJO> sortedPrice = stockInfoService.getSortedPriceForSymbol("IBM");
        System.out.println(sortedPrice);
        System.out.println(sortedPrice.size());
    }

    @Test
    public void sortCompanyByMarket() {
        List<StockCompanyPOJO> sortedCompany = stockInfoService.sortCompanyByMarket();
        System.out.println(sortedCompany);
        System.out.println(sortedCompany.size());
    }

    @Test
    public void sortCompanyByEmployee() {
        List<StockCompanyPOJO> sortedCompany = stockInfoService.sortCompanyByEmployee();
        System.out.println(sortedCompany);
        System.out.println(sortedCompany.size());
    }

    @Test
    public void getAllSymbols() {
        List<String> symbols = stockInfoService.getAllSymbols();
        System.out.println(symbols);
        System.out.println(symbols.size());
    }
}