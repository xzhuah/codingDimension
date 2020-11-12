package nodes.stockinfoNode.impls;

import com.google.inject.Inject;
import nodes.stockinfoNode.PriceAutoUpdater;
import nodes.stockinfoNode.querier.StockPriceDBService;

import java.util.List;
import java.util.Map;

/**
 * Created by Xinyu Zhu on 2020/11/6, 23:50
 * nodes.stockinfoNode.impls in codingDimensionTemplate
 */
public class PriceAutoUpdaterImpl implements PriceAutoUpdater {

    private final StockPriceDBService stockPriceDBService;

    @Inject
    private PriceAutoUpdaterImpl(StockPriceDBService stockPriceDBService) {
        this.stockPriceDBService = stockPriceDBService;
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
