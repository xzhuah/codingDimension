package nodes.stockinfoNode.impls;

import com.google.inject.Inject;
import nodes.stockinfoNode.PriceAutoUpdater;
import nodes.stockinfoNode.StockPriceService;
import nodes.stockinfoNode.querier.StockPriceDBService;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:51
 * nodes.stockinfoNode.impls in codingDimensionTemplate
 */
public class StockPriceServiceImpl implements StockPriceService {

    private final PriceAutoUpdater priceAutoUpdater;
    private final StockPriceDBService stockPriceDBService;

    @Inject
    private StockPriceServiceImpl(PriceAutoUpdater priceAutoUpdater, StockPriceDBService stockPriceDBService) {
        this.priceAutoUpdater = priceAutoUpdater;
        this.stockPriceDBService = stockPriceDBService;
    }


}
