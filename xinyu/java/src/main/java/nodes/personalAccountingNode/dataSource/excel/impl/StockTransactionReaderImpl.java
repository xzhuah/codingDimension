package nodes.personalAccountingNode.dataSource.excel.impl;

import common.io.file.ExcelClient;
import common.time.TimeClient;
import common.time.TimeConstant;
import nodes.personalAccountingNode.dataSource.excel.StockTranscationReader;
import nodes.personalAccountingNode.models.StockTransactionPOJO;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/18, 23:10
 * nodes.personalAccountingNode.dataSource.excel.impl in codingDimensionTemplate
 */
public class StockTransactionReaderImpl implements StockTranscationReader {

    @Override
    public List<StockTransactionPOJO> readMyStockTransactionsInTimeOrder(String excelFile, String tableName) throws IOException, ParseException {
        Map<String, List<String>> data = ExcelClient.readExcelTableByRowAsMapToColumn(excelFile, tableName);

        List<String> dateList = data.get("日期");
        List<String> symbolList = data.get("股票");
        List<String> opList = data.get("操作");
        List<String> priceList = data.get("单价");

        List<StockTransactionPOJO> result = new ArrayList<>();

        for (int i = 0; i < dateList.size(); i++) {
            StockTransactionPOJO stockTransactionPOJO = new StockTransactionPOJO();
            stockTransactionPOJO.setPrimaryId(i);
            stockTransactionPOJO.setTime(ExcelClient.convertExcelTimeToMilliTimestamp(Double.valueOf(dateList.get(i)).longValue()));
            if (i > 0) {
                checkStatus(stockTransactionPOJO.getTime() >= result.get(i - 1).getTime(),
                        String.format("You might have some error in your data source, transaction is not ordered by time %s, %s",
                                stockTransactionPOJO.getTime(), result.get(i - 1).getTime()));
            }
            stockTransactionPOJO.setAccount(symbolList.get(i));
            stockTransactionPOJO.setShares(Double.valueOf(opList.get(i)));
            stockTransactionPOJO.setAmount(stockTransactionPOJO.getShares() * Double.valueOf(priceList.get(i)));
            result.add(stockTransactionPOJO);
        }

        return result;
    }
}
