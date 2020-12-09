package nodes.stockinfoNode.db;

import com.mongodb.client.MongoCollection;
import nodes.stockinfoNode.models.ChineseStockCompanyPOJO;
import nodes.stockinfoNode.models.ChineseStockDailyRecordPOJO;
import org.bson.conversions.Bson;

import java.util.List;

public interface ChineseStockInfoDBService {
    // Query company info
    List<ChineseStockCompanyPOJO> queryCompany(Bson bsonFilter);

    // Query price info
    List<ChineseStockDailyRecordPOJO> queryPrice(Bson bsonFilter);

    // To support other complex query
    MongoCollection<ChineseStockCompanyPOJO> getCompanyInfoCollection();

    // To support other complex query
    MongoCollection<ChineseStockDailyRecordPOJO> getPriceCollection();

    void shutdown();
}
