package nodes.mapNode.china.db.impls;

import com.google.inject.Inject;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import common.io.database.mongodb.MongoDBPojoClient;
import nodes.mapNode.china.constants.ChinaMapConstant;
import nodes.mapNode.china.db.ChineseLevel5AreaInfoDBService;
import nodes.mapNode.china.models.ChineseLevel5AreaInfoPOJO;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/29, 20:05
 * nodes.mapNode.china.db.impls in codingDimensionTemplate
 */
public class ChineseLevel5AreaInfoDBServiceImpl implements ChineseLevel5AreaInfoDBService {
    private final MongoCollection<ChineseLevel5AreaInfoPOJO> targetCollection;

    @Inject
    public ChineseLevel5AreaInfoDBServiceImpl(MongoDBPojoClient mongoDBClient) {
        targetCollection =
                mongoDBClient.getCollection(ChinaMapConstant.DEFAULT_DATABASE,
                        ChinaMapConstant.CHINESE_LEVEL_5_COLLECTION,
                        ChineseLevel5AreaInfoPOJO.class);
    }

    @Override
    public void insertAll(List<ChineseLevel5AreaInfoPOJO> allAreaInfo) {
        targetCollection.insertMany(allAreaInfo);
    }

    @Override
    public List<ChineseLevel5AreaInfoPOJO> query(Bson query) {
        List<ChineseLevel5AreaInfoPOJO> allAreaResult = new ArrayList<>();
        Block<ChineseLevel5AreaInfoPOJO> insertAreaBlock = allAreaResult::add;
        targetCollection.find(query).forEach(insertAreaBlock);
        return allAreaResult;
    }
}
