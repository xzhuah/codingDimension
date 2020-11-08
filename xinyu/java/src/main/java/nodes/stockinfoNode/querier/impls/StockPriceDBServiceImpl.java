package nodes.stockinfoNode.querier.impls;

import common.io.database.mongodb.MongoDBClient;
import common.io.database.mongodb.impl.MongoDBPojoClientImpl;

import java.util.List;
import java.util.Set;


/**
 * Created by Xinyu Zhu on 2020/11/7, 2:10
 * nodes.stockinfoNode.querier.impls in codingDimensionTemplate
 */
public class StockPriceDBServiceImpl {
    private static StockPriceDBServiceImpl instance = null;
    private MongoDBClient mongoDBClient;

    private StockPriceDBServiceImpl() {
        mongoDBClient = new MongoDBPojoClientImpl();
    }

    public static StockPriceDBServiceImpl getInstance() {
        if (instance == null) {
            synchronized (StockPriceDBServiceImpl.class) {
                if (instance == null) {
                    instance = new StockPriceDBServiceImpl();
                }
            }
        }
        return instance;
    }

    public List<String> queryAllSymbol() {
        // TODO
        return null;
    }

    public void insertSymbols(Set<String> symbols) {
        // TODO
    }

    // TODO add other method

}
