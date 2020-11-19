package nodes.personalAccountingNode.db;

import nodes.personalAccountingNode.models.StockTransactionPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/18, 21:31
 * nodes.personalAccountingNode.db in codingDimensionTemplate
 */
public interface StockTransactionDBService {

    void insert(List<StockTransactionPOJO> stockTransactionPOJOs);

    default void insert(StockTransactionPOJO stockTransactionPOJO) {
        insert(List.of(stockTransactionPOJO));
    }
}
