package nodes.personalAccountingNode;

import com.google.inject.AbstractModule;
import nodes.personalAccountingNode.dataSource.excel.StockTranscationReader;
import nodes.personalAccountingNode.dataSource.excel.impl.StockTransactionReaderImpl;
import nodes.personalAccountingNode.db.StockTransactionDBService;
import nodes.personalAccountingNode.db.StockTransactionUpdater;
import nodes.personalAccountingNode.db.impl.StockTransactionDBServiceImpl;
import nodes.personalAccountingNode.db.impl.StockTransactionUpdaterImpl;

/**
 * Created by Xinyu Zhu on 2020/11/18, 21:15
 * nodes.personalAccountingNode in codingDimensionTemplate
 * <p>
 * The task of this module is to help record different kinds of personal transactions
 * To help monitering personal financial activity
 */
public class PersonalAccountingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StockTranscationReader.class).to(StockTransactionReaderImpl.class);
        bind(StockTransactionDBService.class).to(StockTransactionDBServiceImpl.class);
        bind(StockTransactionUpdater.class).to(StockTransactionUpdaterImpl.class);
    }
}
