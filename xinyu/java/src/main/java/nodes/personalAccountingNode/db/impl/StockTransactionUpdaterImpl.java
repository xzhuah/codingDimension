package nodes.personalAccountingNode.db.impl;

import com.google.inject.Inject;
import nodes.personalAccountingNode.constants.AccountingConstant;
import nodes.personalAccountingNode.dataSource.excel.StockTranscationReader;
import nodes.personalAccountingNode.db.StockTransactionDBService;
import nodes.personalAccountingNode.db.StockTransactionUpdater;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Xinyu Zhu on 2020/11/18, 23:55
 * nodes.personalAccountingNode.db.impl in codingDimensionTemplate
 */
public class StockTransactionUpdaterImpl implements StockTransactionUpdater {

    private final StockTransactionDBService dbService;
    private final StockTranscationReader transcationReader;

    @Inject
    public StockTransactionUpdaterImpl(StockTransactionDBService dbService, StockTranscationReader transcationReader) {
        this.dbService = dbService;
        this.transcationReader = transcationReader;
    }

    @Override
    public void autoUpdate() {
        try {
            dbService.insert(transcationReader.readMyStockTransactionsInTimeOrder(AccountingConstant.STOCK_TRANSACTION_SOURCE_PATH, AccountingConstant.STOCK_TRANSACTION_SOURCE_TABLE));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.err.println("Stock Transaction Updating failed");
        }
    }
}
