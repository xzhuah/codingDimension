package nodes.datascienceNode.stockInfo.utils;

import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/15, 1:13
 * nodes.datascienceNode.stockInfo.utils in codingDimensionTemplate
 */
public class Converter {
    public static List<Double> toDailyAvgPriceList(List<StockDailyRecordPOJO> sortedDailyPriceRecord) {
        List<Double> dailyAvgPriceList = new ArrayList<>(sortedDailyPriceRecord.size());
        sortedDailyPriceRecord.forEach(dailyRecordPOJO -> dailyAvgPriceList.add(toAvgPrice(dailyRecordPOJO)));
        return dailyAvgPriceList;
    }

    public static double toAvgPrice(StockDailyRecordPOJO dailyRecordPOJO) {
        return (dailyRecordPOJO.getOpen() + dailyRecordPOJO.getHigh() + dailyRecordPOJO.getLow() + dailyRecordPOJO.getClose()) / 4.0;
    }
}
