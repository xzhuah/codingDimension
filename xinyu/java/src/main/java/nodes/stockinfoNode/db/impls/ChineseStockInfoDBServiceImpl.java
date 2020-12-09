package nodes.stockinfoNode.db.impls;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import common.io.database.mongodb.MongoDBPojoClient;
import nodes.NodeModule;
import nodes.stockinfoNode.constants.StockConstant;
import nodes.stockinfoNode.db.ChineseStockInfoDBService;
import nodes.stockinfoNode.models.ChineseStockCompanyPOJO;
import nodes.stockinfoNode.models.ChineseStockDailyRecordPOJO;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by Xinyu Zhu on 2020/12/2, 23:42
 * nodes.stockinfoNode.db.impls in codingDimensionTemplate
 */
@Singleton
public class ChineseStockInfoDBServiceImpl implements ChineseStockInfoDBService {

    private final MongoDBPojoClient mongoDBClient;

    private final MongoCollection<ChineseStockCompanyPOJO> symbolCollection;
    private final MongoCollection<ChineseStockDailyRecordPOJO> priceCollection;

    @Inject
    private ChineseStockInfoDBServiceImpl(MongoDBPojoClient mongoDBClient) {
        this.mongoDBClient = mongoDBClient;

        symbolCollection = this.mongoDBClient.getCollection(StockConstant.DEFAULT_DATABASE,
                StockConstant.CHINESE_SYMBOL_COLLECTION, ChineseStockCompanyPOJO.class);
        priceCollection = this.mongoDBClient.getCollection(StockConstant.DEFAULT_DATABASE,
                StockConstant.CHINESE_PRICE_COLLECTION, ChineseStockDailyRecordPOJO.class);
    }

    @Override
    public List<ChineseStockCompanyPOJO> queryCompany(Bson bsonFilter) {
        List<ChineseStockCompanyPOJO> allCompanies = new ArrayList<>();
        Block<ChineseStockCompanyPOJO> insertCompanyBlock = allCompanies::add;
        symbolCollection.find(bsonFilter).forEach(insertCompanyBlock);
        return allCompanies;
    }

    @Override
    public List<ChineseStockDailyRecordPOJO> queryPrice(Bson bsonFilter) {
        List<ChineseStockDailyRecordPOJO> allPriceRecord = new ArrayList<>();
        Block<ChineseStockDailyRecordPOJO> insertPriceBlock = allPriceRecord::add;
        priceCollection.find(bsonFilter).forEach(insertPriceBlock);
        return allPriceRecord;
    }

    @Override
    public MongoCollection<ChineseStockCompanyPOJO> getCompanyInfoCollection() {
        return this.mongoDBClient.getCollection(StockConstant.DEFAULT_DATABASE,
                StockConstant.CHINESE_SYMBOL_COLLECTION, ChineseStockCompanyPOJO.class);
    }

    @Override
    public MongoCollection<ChineseStockDailyRecordPOJO> getPriceCollection() {
        return this.mongoDBClient.getCollection(StockConstant.DEFAULT_DATABASE,
                StockConstant.CHINESE_PRICE_COLLECTION, ChineseStockDailyRecordPOJO.class);
    }

    @Override
    public void shutdown() {
        this.mongoDBClient.close();
    }

    public static void main(String[] args) {
        ChineseStockInfoDBService chineseStockInfoDBService =  NodeModule.getGlobalInjector().getInstance(ChineseStockInfoDBService.class);
        List<ChineseStockDailyRecordPOJO> result = chineseStockInfoDBService.queryPrice(eq("ts_code", "000001.SZ"));
        System.out.println(result.size());
        System.out.println(result.get(0));
    }
}
