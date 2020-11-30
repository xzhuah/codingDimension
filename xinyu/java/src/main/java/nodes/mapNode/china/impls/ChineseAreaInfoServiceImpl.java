package nodes.mapNode.china.impls;

import com.google.inject.Inject;
import nodes.mapNode.china.db.ChineseLevel5AreaInfoDBService;
import nodes.mapNode.china.models.ChineseLevel5AreaInfoPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/29, 22:04
 * nodes.mapNode.china.impls in codingDimensionTemplate
 */
public class ChineseAreaInfoServiceImpl {

    private final ChineseLevel5AreaInfoDBService chineseLevel5AreaInfoDBService;

    @Inject
    ChineseAreaInfoServiceImpl(ChineseLevel5AreaInfoDBService chineseLevel5AreaInfoDBService) {
        this.chineseLevel5AreaInfoDBService = chineseLevel5AreaInfoDBService;
    }

    // 查找到给定坐标
    List<ChineseLevel5AreaInfoPOJO> queryCityWithinRadius(double centerLng, double centerLat, double radius) {
        return null;
    }
}
