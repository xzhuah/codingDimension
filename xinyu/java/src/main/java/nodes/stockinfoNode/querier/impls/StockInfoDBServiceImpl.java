package nodes.stockinfoNode.querier.impls;

import com.google.inject.Inject;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import common.io.database.mongodb.MongoDBPojoClient;
import nodes.stockinfoNode.constants.StockConstant;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import nodes.stockinfoNode.querier.StockInfoDBService;
import nodes.stockinfoNode.utils.Converter;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Xinyu Zhu on 2020/11/7, 2:10
 * nodes.stockinfoNode.querier.impls in codingDimensionTemplate
 */
public class StockInfoDBServiceImpl implements StockInfoDBService {
    private final MongoDBPojoClient mongoDBClient;

    private final MongoCollection<StockCompanyPOJO> symbolCollection;
    private final MongoCollection<StockDailyRecordPOJO> priceCollection;

    @Inject
    private StockInfoDBServiceImpl(MongoDBPojoClient mongoDBClient) {
        this.mongoDBClient = mongoDBClient;

        symbolCollection =  this.mongoDBClient.getCollection(StockConstant.DEFAULT_DATABASE,
                StockConstant.SYMBOL_COLLECTION, StockCompanyPOJO.class);
        priceCollection =  this.mongoDBClient.getCollection(StockConstant.DEFAULT_DATABASE,
                StockConstant.PRICE_COLLECTION, StockDailyRecordPOJO.class);
    }

    @Override
    public List<StockCompanyPOJO> queryCompany(final Bson bsonFilter) {
        List<StockCompanyPOJO> allCompanies = new ArrayList<>();

        Block<StockCompanyPOJO> insertCompanyBlock = allCompanies::add;
        symbolCollection.find(bsonFilter).forEach(insertCompanyBlock);
        return allCompanies;
    }

    @Override
    public List<StockDailyRecordPOJO> queryPrice(final Bson bsonFilter) {
        List<StockDailyRecordPOJO> allPriceRecord = new ArrayList<>();
        Block<StockDailyRecordPOJO> insertPriceBlock = allPriceRecord::add;
        priceCollection.find(bsonFilter).forEach(insertPriceBlock);
        return allPriceRecord;
    }

    @Override
    public void insertCompany(final List<StockCompanyPOJO> companies) {
        for (StockCompanyPOJO stockCompanyPOJO : companies) {
            try {
                symbolCollection.insertOne(stockCompanyPOJO);
            } catch (Exception duplicateException) {
                if (StockConstant.OVERRIDE_WHEN_UPDATE) {
                    try {
                        symbolCollection.findOneAndDelete(Converter.toPrimaryFilter(stockCompanyPOJO));
                        symbolCollection.insertOne(stockCompanyPOJO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void insertPrice(final List<StockDailyRecordPOJO> prices) {
        for (StockDailyRecordPOJO stockDailyRecordPOJO : prices) {
            try {
                priceCollection.insertOne(stockDailyRecordPOJO);
            } catch (Exception duplicateException) {
                if (StockConstant.OVERRIDE_WHEN_UPDATE) {
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
    }

    // To support other complex query
    @Override
    public MongoCollection<StockCompanyPOJO>  getCompanyInfoCollection() {
        return this.mongoDBClient.getCollection(StockConstant.DEFAULT_DATABASE, StockConstant.SYMBOL_COLLECTION, StockCompanyPOJO.class);
    }

    // To support other complex query
    @Override
    public MongoCollection<StockDailyRecordPOJO> getPriceCollection() {
        return this.mongoDBClient.getCollection(StockConstant.DEFAULT_DATABASE,StockConstant.PRICE_COLLECTION, StockDailyRecordPOJO.class);
    }

    @Override
    public void shutdown() {
        this.mongoDBClient.close();
    }

}
