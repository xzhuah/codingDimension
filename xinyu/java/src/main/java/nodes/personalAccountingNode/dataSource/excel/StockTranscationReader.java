package nodes.personalAccountingNode.dataSource.excel;

import nodes.personalAccountingNode.models.StockTransactionPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/18, 21:27
 * nodes.personalAccountingNode.dataSource.excel in codingDimensionTemplate
 */
public interface StockTranscationReader {
    List<StockTransactionPOJO> readMyStockTransactionsInTimeOrder(String excelFile, String tableName);
}
