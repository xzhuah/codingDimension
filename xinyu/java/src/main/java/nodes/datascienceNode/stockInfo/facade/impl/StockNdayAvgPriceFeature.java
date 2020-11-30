package nodes.datascienceNode.stockInfo.facade.impl;

import lombok.Setter;
import nodes.datascienceNode.common.utils.Algorithms;
import nodes.datascienceNode.stockInfo.utils.Converter;
import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/21, 1:57
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 */
@Setter
public class StockNdayAvgPriceFeature implements Feature<List<StockDailyRecordPOJO>, List<Double>> {
    private int nDay;

    public StockNdayAvgPriceFeature(int nDay) {
        this.nDay = nDay;
    }

    @Override
    public List<Double> extractForInstance(List<StockDailyRecordPOJO> target) {
        List<Double> dailyAvg = Converter.toDailyAvgPriceList(target);
        List<Double> result = Algorithms.slidingWindowAvg(Algorithms.listPadding(dailyAvg,nDay - 1,  Algorithms.PaddingPolicy.LEFT), nDay);
        return result;
    }

    @Override
    public String getFeatureName() {
       return String.format("%sday avg", this.nDay);
    }
}
