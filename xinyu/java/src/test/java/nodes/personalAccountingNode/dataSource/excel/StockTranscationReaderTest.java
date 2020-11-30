package nodes.personalAccountingNode.dataSource.excel;

import nodes.NodeModule;
import nodes.personalAccountingNode.constants.AccountingConstant;
import nodes.personalAccountingNode.models.StockTransactionPOJO;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Xinyu Zhu on 2020/11/18, 23:21
 * nodes.personalAccountingNode.dataSource.excel in codingDimensionTemplate
 */
public class StockTranscationReaderTest {

    StockTranscationReader stockTranscationReader = NodeModule.getGlobalInjector().getInstance(StockTranscationReader.class);

    @Test
    public void readMyStockTransactionsInTimeOrder() throws IOException, ParseException {
        List<StockTransactionPOJO> result
                = stockTranscationReader.readMyStockTransactionsInTimeOrder(
                AccountingConstant.STOCK_TRANSACTION_SOURCE_PATH,
                AccountingConstant.STOCK_TRANSACTION_SOURCE_TABLE);

        System.out.println(result);

        assertTrue(result.size() > 0);
    }
}