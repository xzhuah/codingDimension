package nodes.datascienceNode.stockInfo.facade.impl;

import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/29, 1:10
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 * <p>
 * 均线斜率, 实际上就是当天平均价格减去前一天平均价格的差
 */
public class StockNdayAvgPriceSlopeFeature implements Feature<List<StockDailyRecordPOJO>, List<Double>> {
    private StockNdayAvgPriceFeature stockNdayAvgPriceFeature;

    public StockNdayAvgPriceSlopeFeature(int nDay) {
        this.stockNdayAvgPriceFeature = new StockNdayAvgPriceFeature(nDay);
    }

    @Override
    public List<Double> extractForInstance(List<StockDailyRecordPOJO> target) {
        List<Double> avgPrice = stockNdayAvgPriceFeature.extractForInstance(target);
        List<Double> avgPriceSlope = new ArrayList<>(avgPrice);
        avgPriceSlope.add(0D);
        for (int i = 1; i < avgPrice.size(); i++) {
            avgPriceSlope.add(avgPrice.get(i) - avgPrice.get(i - 1));
        }
        return avgPriceSlope;
    }
}
