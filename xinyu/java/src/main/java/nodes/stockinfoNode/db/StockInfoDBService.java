package nodes.stockinfoNode.db;

import com.mongodb.client.MongoCollection;
import nodes.stockinfoNode.models.StockCompanyPOJO;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/11, 20:00
 * nodes.stockinfoNode.querier in codingDimensionTemplate
 * <p>
 * This Interface provides the basic operations needed to interact with the database in stock price business
 */
public interface StockInfoDBService {
    // Query company info
    List<? extends StockCompanyPOJO> queryCompany(Bson bsonFilter);

    // Query price info
    List<? extends StockDailyRecordPOJO> queryPrice(Bson bsonFilter);

    // Insert a list of company, auto deduplication if the database has correct unique index
    void insertCompany(List<StockCompanyPOJO> companies);

    // Insert a list of price record, auto deduplication if the database has correct unique index
    void insertPrice(List<StockDailyRecordPOJO> prices);

    default void insertCompany(StockCompanyPOJO companyPOJO) {
        insertCompany(List.of(companyPOJO));
    }

    default void insertPrice(StockDailyRecordPOJO price) {
        insertPrice(List.of(price));
    }

    // To support other complex query
    MongoCollection<? extends StockCompanyPOJO> getCompanyInfoCollection();

    // To support other complex query
    MongoCollection<? extends StockDailyRecordPOJO> getPriceCollection();

    void shutdown();
}
