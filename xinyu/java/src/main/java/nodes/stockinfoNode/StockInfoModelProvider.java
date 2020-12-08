package nodes.stockinfoNode;

import common.framework.BaseModelProvider;
import nodes.stockinfoNode.models.ChineseStockCompanyPOJO;
import nodes.stockinfoNode.models.ChineseStockDailyRecordPOJO;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

/**
 * Created by Xinyu Zhu on 2020/12/7, 22:00
 * nodes.stockinfoNode in codingDimensionTemplate
 */
public class StockInfoModelProvider extends BaseModelProvider {
    @Override
    protected void configure() {
        super.configure();
        this.register(StockDailyRecordPOJO.class);
        this.register(StockCompanyPOJO.class);
        this.register(ChineseStockDailyRecordPOJO.class);
        this.register(ChineseStockCompanyPOJO.class);
    }
}
