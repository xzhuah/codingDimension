package nodes.mapNode.china;

import nodes.mapNode.china.models.ChineseLevel5AreaInfoPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/30, 0:01
 * nodes.mapNode.china in codingDimensionTemplate
 */
public interface ChineseAreaInfoService {
    List<ChineseLevel5AreaInfoPOJO> queryCityWithinRadius(double centerLng, double centerLat, double radius, boolean sort);

    default List<ChineseLevel5AreaInfoPOJO> queryCityWithinRadius(double centerLng, double centerLat, double radius) {
        // by default don't sort
        return queryCityWithinRadius(centerLng, centerLat, radius, false);
    }
}
