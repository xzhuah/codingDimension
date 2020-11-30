package nodes.mapNode.china.utils;

import nodes.mapNode.china.models.ChineseLevel5AreaInfoPOJO;
import nodes.mapNode.models.Position;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by Xinyu Zhu on 2020/11/29, 20:17
 * nodes.mapNode.china.utils in codingDimensionTemplate
 */
public class Converter {

    private static Pattern linePattern = Pattern.compile("([0-9]+),([0-9]+),([0-9]+),([0-9]+),([0-9]+),\'([0-9]*)\',\'([^\']+)\',\'([^\']+)\',\'([^\']+)\',\'([\\s\\S]*)\',([0-9.]+),([0-9.]+)");

    // linerecord: (783535,3,7113126346648,7111551038080,655024,'05','中山路','中山路','台湾省,云林,元长,中山路','ZhongShanLu',120.480738,23.664943),
    public static ChineseLevel5AreaInfoPOJO toChineseLevel5AreaInfoPOJO(String recordLine) {
        ChineseLevel5AreaInfoPOJO chineseLevel5AreaInfoPOJO = new ChineseLevel5AreaInfoPOJO();
        Matcher m = linePattern.matcher(recordLine);

        if (m.find()) {
            chineseLevel5AreaInfoPOJO.setLevel(Integer.parseInt(m.group(2)));
            chineseLevel5AreaInfoPOJO.setParentCode(m.group(3));
            chineseLevel5AreaInfoPOJO.setAreaCode(m.group(4));
            chineseLevel5AreaInfoPOJO.setZipCode(m.group(5));
            chineseLevel5AreaInfoPOJO.setCityCode(m.group(6));
            chineseLevel5AreaInfoPOJO.setName(m.group(7));
            chineseLevel5AreaInfoPOJO.setShortName(m.group(8));
            chineseLevel5AreaInfoPOJO.setMergerName(m.group(9));
            chineseLevel5AreaInfoPOJO.setPinyin(m.group(10));
            chineseLevel5AreaInfoPOJO.setLng(Double.parseDouble(m.group(11)));
            chineseLevel5AreaInfoPOJO.setLat(Double.parseDouble(m.group(12)));
        } else {
            System.err.println("Error parsing:" + recordLine);
            return null;
        }

        return chineseLevel5AreaInfoPOJO;
    }

    public static List<ChineseLevel5AreaInfoPOJO> toChineseLevel5AreaInfoPOJOs(String content) {
        String[] allRecords = content.split("\n");
        List<ChineseLevel5AreaInfoPOJO> result = new ArrayList<>(allRecords.length);
        for (String record : allRecords) {
            if (record.length() > 0) {
                ChineseLevel5AreaInfoPOJO chineseLevel5Record = toChineseLevel5AreaInfoPOJO(record);
                if (null != chineseLevel5Record) {
                    result.add(toChineseLevel5AreaInfoPOJO(record));
                }
            }
        }
        return result;
    }

    public static Bson toAreaFilter(Position north, Position east, Position south, Position west) {
        // 考虑0度经线引起的错误
        Bson latFilter = and(gte("lat", south.getLat()), lte("lat", north.getLat()));

        Bson lngFilter;
        if (east.getLng() < west.getLng()) {
            // 跨过了0度经线
            lngFilter = and(gte("lng", west.getLng()), lte("lng", 180),
                    gte("lng", -180), lte("lng", east.getLng()));
        } else {
            lngFilter = and(gte("lng", west.getLng()), lte("lng", east.getLng()));
        }
        return and(latFilter, lngFilter);
    }

    public static void main(String[] args) throws IOException {


    }
}
