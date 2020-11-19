package nodes.personalAccountingNode.db.impl;

import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import common.io.database.mongodb.MongoDBPojoClient;
import nodes.personalAccountingNode.constants.AccountingConstant;
import nodes.personalAccountingNode.db.StockTransactionDBService;
import nodes.personalAccountingNode.models.StockTransactionPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/18, 23:47
 * nodes.personalAccountingNode.db.impl in codingDimensionTemplate
 */
public class StockTransactionDBServiceImpl implements StockTransactionDBService {
    private final MongoDBPojoClient mongoDBPojoClient;
    private final MongoCollection<StockTransactionPOJO> transactionCollection;

    @Inject
    public StockTransactionDBServiceImpl(MongoDBPojoClient mongoDBPojoClient) {
        this.mongoDBPojoClient = mongoDBPojoClient;
        this.transactionCollection = this.mongoDBPojoClient.getCollection(AccountingConstant.ACCOUNTING_DATABASE,
                AccountingConstant.STOCK_TRANSACTION_COLLECTION, StockTransactionPOJO.class);
    }
    @Override
    public void insert(List<StockTransactionPOJO> stockTransactionPOJOs) {

        for (StockTransactionPOJO stockTransactionPOJO : stockTransactionPOJOs) {
            try {
                transactionCollection.insertOne(stockTransactionPOJO);
            } catch (Exception duplicationException) {
                // Just continue;
            }
        }
    }
}
