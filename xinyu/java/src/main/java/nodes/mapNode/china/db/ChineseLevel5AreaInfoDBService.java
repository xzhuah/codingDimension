package nodes.mapNode.china.db;

import nodes.mapNode.china.models.ChineseLevel5AreaInfoPOJO;
import org.bson.conversions.Bson;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/29, 20:01
 * nodes.mapNode.china.db in codingDimensionTemplate
 * <p>
 * 由于数据是完全提供的, 所以采用全局更新, 一次insertMany达到最大效率
 */
public interface ChineseLevel5AreaInfoDBService {
    public void insertAll(List<ChineseLevel5AreaInfoPOJO> allAreaInfo);

    public List<ChineseLevel5AreaInfoPOJO> query(Bson query);
}
