package nodes.stockinfoNode.impls;

import nodes.stockinfoNode.querier.impls.StockPriceDBServiceImpl;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:51
 * nodes.stockinfoNode.impls in codingDimensionTemplate
 */
public class StockPriceServiceImpl {
    PriceAutoUpdaterImpl priceAutoUpdater;
    StockPriceDBServiceImpl stockPriceDBService;

    private static StockPriceServiceImpl instance = null;

    private StockPriceServiceImpl() {
        priceAutoUpdater = PriceAutoUpdaterImpl.getInstance();
        stockPriceDBService = StockPriceDBServiceImpl.getInstance();
    }

    public static StockPriceServiceImpl getInstance() {
        if (instance == null) {
            synchronized (StockPriceServiceImpl.class) {
                if (instance == null) {
                    instance = new StockPriceServiceImpl();
                }
            }
        }
        return instance;
    }
}
