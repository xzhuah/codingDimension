package nodes.stockinfoNode.impls;

import com.google.inject.Inject;
import nodes.stockinfoNode.PriceAutoUpdater;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.querier.StockInfoDBService;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:51
 * nodes.stockinfoNode.impls in codingDimensionTemplate
 */
public class StockInfoServiceImpl implements StockInfoService {

    private final PriceAutoUpdater priceAutoUpdater;
    private final StockInfoDBService stockInfoDBService;

    @Inject
    private StockInfoServiceImpl(PriceAutoUpdater priceAutoUpdater, StockInfoDBService stockInfoDBService) {
        this.priceAutoUpdater = priceAutoUpdater;
        this.stockInfoDBService = stockInfoDBService;
    }




}
