package nodes.mapNode.china.impls;

import com.google.inject.Inject;
import nodes.NodeModule;
import nodes.mapNode.china.ChineseAreaInfoService;
import nodes.mapNode.china.db.ChineseLevel5AreaInfoDBService;
import nodes.mapNode.china.models.ChineseLevel5AreaInfoPOJO;
import nodes.mapNode.china.utils.Converter;
import nodes.mapNode.models.Position;
import nodes.mapNode.utils.BasicGeoTools;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/29, 22:04
 * nodes.mapNode.china.impls in codingDimensionTemplate
 */
public class ChineseAreaInfoServiceImpl implements ChineseAreaInfoService {

    private final ChineseLevel5AreaInfoDBService chineseLevel5AreaInfoDBService;

    @Inject
    ChineseAreaInfoServiceImpl(ChineseLevel5AreaInfoDBService chineseLevel5AreaInfoDBService) {
        this.chineseLevel5AreaInfoDBService = chineseLevel5AreaInfoDBService;
    }

    // 查找到给定坐标距离半径小于等于radius(单位米)的所有区域
    public List<ChineseLevel5AreaInfoPOJO> queryCityWithinRadius(double centerLng, double centerLat, double radius, boolean sort) {
        // 构造一个方格, 选取方格内所有区域 (优化数据库查询操作)
        // 遍历这些城市, 过滤距离小于等于半径的地区
        Position center = new Position(centerLng, centerLat);


        Position northPoint = BasicGeoTools.moveToNorth(center, radius);
        Position eastPoint = BasicGeoTools.moveToEast(center, radius);
        Position southPoint = BasicGeoTools.moveToSouth(center, radius);
        Position westPoint = BasicGeoTools.moveToWest(center, radius);

        Bson filter = Converter.toAreaFilter(northPoint, eastPoint, southPoint, westPoint);
        System.out.println(filter);
        List<ChineseLevel5AreaInfoPOJO> resultInBlock = chineseLevel5AreaInfoDBService.query(filter);

        List<ChineseLevel5AreaInfoPOJO> result = new ArrayList<>((int) (resultInBlock.size() * 3.14 / 4));
        for (ChineseLevel5AreaInfoPOJO chineseLevel5AreaInfoPOJO : resultInBlock) {
            if (BasicGeoTools.getDistance(chineseLevel5AreaInfoPOJO.getLng(), chineseLevel5AreaInfoPOJO.getLat(), centerLng, centerLat) <= radius) {
                result.add(chineseLevel5AreaInfoPOJO);
            }
        }

        if (sort) {
            result.sort(Comparator.comparingDouble(area -> BasicGeoTools.getDistance(centerLng, centerLat, area.getLng(), area.getLat())));
        }

        return result;
    }

    public static void main(String[] args) {
        ChineseAreaInfoService chineseAreaInfoService = NodeModule.getGlobalInjector().getInstance(ChineseAreaInfoService.class);
        List<ChineseLevel5AreaInfoPOJO> result = chineseAreaInfoService.queryCityWithinRadius(121.642931, 38.913463, 20000, true);
        for (ChineseLevel5AreaInfoPOJO chineseLevel5AreaInfoPOJO : result) {
            if (chineseLevel5AreaInfoPOJO.getLevel() <= 2) {
                System.out.println(chineseLevel5AreaInfoPOJO);
            }
        }
    }
}
