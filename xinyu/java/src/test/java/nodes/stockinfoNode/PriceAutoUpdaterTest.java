package nodes.stockinfoNode;

import nodes.NodeModule;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.utils.Converter;
import org.junit.Test;

import java.util.List;


/**
 * Created by Xinyu Zhu on 2020/11/11, 21:42
 * nodes.stockinfoNode in codingDimensionTemplate
 */
public class PriceAutoUpdaterTest {

    PriceAutoUpdater autoUpdater = NodeModule.getGlobalInjector().getInstance(PriceAutoUpdater.class);

    @Test
    public void test() {
        System.out.println(autoUpdater.isOutOfDate(Converter.toStockCompanyPOJO("IBM")));
        List<StockCompanyPOJO> outOfDateCompany = autoUpdater.getOutOfDateCompany();
        System.out.println(outOfDateCompany.size());
    }

    @Test
    public void updateTest() {
        autoUpdater.fullUpdate();
    }
}