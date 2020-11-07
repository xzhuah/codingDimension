package nodes.stockinfoNode.impls;

import nodes.stockinfoNode.querier.impls.StockPriceDBServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:50
 * nodes.stockinfoNode.impls in codingDimensionTemplate
 */
public class PriceAutoUpdaterImpl {
    StockPriceDBServiceImpl stockPriceDBService;

    private static PriceAutoUpdaterImpl instance = null;

    private PriceAutoUpdaterImpl() {
        stockPriceDBService = StockPriceDBServiceImpl.getInstance();
    }

    public static PriceAutoUpdaterImpl getInstance() {
        if (instance == null) {
            synchronized (PriceAutoUpdaterImpl.class) {
                if (instance == null) {
                    instance = new PriceAutoUpdaterImpl();
                }
            }
        }
        return instance;
    }


    public List<String> needOutOfDateSymbols() {
        // TODO
        return null;
    }

    public boolean needUpdate() {
        // TODO
        return !needOutOfDateSymbols().isEmpty();
    }

    public void update() {
        // TODO
    }

    public Map<String, Long> getLastUpdateTimestamp() {
        // TODO
        return null;
    }
}
