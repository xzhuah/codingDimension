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

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.not;

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
        // 构造一个外接方格, 选取方格内所有区域 (优化数据库查询操作)
        // 遍历这些城市, 过滤距离小于等于半径的地区
        Position center = new Position(centerLng, centerLat);

        // 外接方格
        Position northPoint = BasicGeoTools.moveToNorth(center, radius);
        Position eastPoint = BasicGeoTools.moveToEast(center, radius);
        Position southPoint = BasicGeoTools.moveToSouth(center, radius);
        Position westPoint = BasicGeoTools.moveToWest(center, radius);

        // 内接方格
        Position inNorthPoint = BasicGeoTools.moveToNorth(center, radius / Math.pow(2, 0.5));
        Position inEastPoint = BasicGeoTools.moveToEast(center, radius / Math.pow(2, 0.5));
        Position inSouthPoint = BasicGeoTools.moveToSouth(center, radius / Math.pow(2, 0.5));
        Position inWestPoint = BasicGeoTools.moveToWest(center, radius / Math.pow(2, 0.5));

        Bson filter = Converter.toAreaFilter(northPoint, eastPoint, southPoint, westPoint);

        Bson inFilter = Converter.toAreaFilter(inNorthPoint, inEastPoint, inSouthPoint, inWestPoint);
        Bson revertInFilter = Converter.toOutSizeAreaFilter(inNorthPoint, inEastPoint, inSouthPoint, inWestPoint);

        List<ChineseLevel5AreaInfoPOJO> resultInInnerBlock = chineseLevel5AreaInfoDBService.query(inFilter);
        List<ChineseLevel5AreaInfoPOJO> resultBetweenInnerAndOuterBlock = chineseLevel5AreaInfoDBService.query(and(revertInFilter, filter));
        List<ChineseLevel5AreaInfoPOJO> result = new ArrayList<>((int) (resultBetweenInnerAndOuterBlock.size() * (Math.PI - 2) / 2));
        for (ChineseLevel5AreaInfoPOJO chineseLevel5AreaInfoPOJO : resultBetweenInnerAndOuterBlock) {
            if (BasicGeoTools.getDistance(chineseLevel5AreaInfoPOJO.getLng(), chineseLevel5AreaInfoPOJO.getLat(), centerLng, centerLat) <= radius) {
                result.add(chineseLevel5AreaInfoPOJO);
            }
        }
        result.addAll(resultInInnerBlock);

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
