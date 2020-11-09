package nodes.stockinfoNode.querier.impls;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import common.io.database.mongodb.MongoDBClient;
import common.io.database.mongodb.impl.MongoDBPojoClientImpl;
import nodes.stockinfoNode.constants.StockConstant;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import nodes.stockinfoNode.utils.Converter;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Created by Xinyu Zhu on 2020/11/7, 2:10
 * nodes.stockinfoNode.querier.impls in codingDimensionTemplate
 */
public class StockPriceDBServiceImpl {
    private static StockPriceDBServiceImpl instance = null;
    // TODO use interface
    private MongoDBPojoClientImpl mongoDBClient;
    private MongoCollection symbolCollection;
    private MongoCollection priceCollection;

    private StockPriceDBServiceImpl() {
        mongoDBClient = new MongoDBPojoClientImpl();
        mongoDBClient.setCurrentDatabase(StockConstant.DEFAULT_DATABASE);
        symbolCollection = mongoDBClient.setAndGetCurrentCollection(StockConstant.SYMBOL_COLLECTION);
        priceCollection = mongoDBClient.setAndGetCurrentCollection(StockConstant.PRICE_COLLECTION);
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

    public List<StockCompanyPOJO> queryAllCompany() {
        List<StockCompanyPOJO> allCompanies = new ArrayList<>();

        Block<StockCompanyPOJO> insertCompanyBlock = allCompanies::add;
        symbolCollection.find().forEach(insertCompanyBlock);
        return allCompanies;
    }

    public void insertCompany(final List<StockCompanyPOJO> companies) {
        for (StockCompanyPOJO stockCompanyPOJO : companies) {
            try {
                symbolCollection.insertOne(stockCompanyPOJO);
            } catch (Exception duplicateException) {
                try {
                    symbolCollection.findOneAndDelete(Converter.toPrimaryFilter(stockCompanyPOJO));
                    symbolCollection.insertOne(stockCompanyPOJO);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    public List<StockDailyRecordPOJO> queryPrice(Bson bsonFilter) {
        List<StockDailyRecordPOJO> allPriceRecord = new ArrayList<>();
        Block<StockDailyRecordPOJO> insertPriceBlock = allPriceRecord::add;
        priceCollection.find(bsonFilter).forEach(insertPriceBlock);
        return allPriceRecord;
    }

    public void insertPrice(final List<StockDailyRecordPOJO> prices) {
        for (StockDailyRecordPOJO stockDailyRecordPOJO : prices) {
            try {
                priceCollection.insertOne(stockDailyRecordPOJO);
            } catch (Exception duplicateException) {
                try {
                    priceCollection.findOneAndDelete(Converter.toPrimaryFilter(stockDailyRecordPOJO));
                    priceCollection.insertOne(stockDailyRecordPOJO);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    public void shutdown() {
        this.mongoDBClient.close();
    }

    // TODO add other method

}
