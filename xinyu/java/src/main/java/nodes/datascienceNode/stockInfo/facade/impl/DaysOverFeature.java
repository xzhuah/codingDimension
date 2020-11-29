package nodes.datascienceNode.stockInfo.facade.impl;

import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.featureEngineeringNode.facade.Feature;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/29, 0:56
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 *
 * 这个类的命名并不好, 实际上是想计算近期10日均价大于5日均价的累计天数, 但这个特征不好描述
 */
public class DaysOverFeature implements ComparableFeature<List<StockDailyRecordPOJO>, Integer> {

    StockNdayAvgPriceFeature nDayAvg;
    StockNdayAvgPriceFeature mDayAvg;

    // 计算m日均价大于n日均价的天数
    public DaysOverFeature(int n, int m) {
        nDayAvg = new StockNdayAvgPriceFeature(n);
        mDayAvg = new StockNdayAvgPriceFeature(m);
    }

    public DaysOverFeature() {
        this(5, 10);
    }

    @Override
    public Integer extractForInstance(List<StockDailyRecordPOJO> target) {
        List<Double> nDayAvgPrice = nDayAvg.extractForInstance(target);
        List<Double> mDayAvgPrice = mDayAvg.extractForInstance(target);
        int n = 0;
        for (int i = nDayAvgPrice.size() - 1; i >= 0; i--) {
            if (mDayAvgPrice.get(i) > nDayAvgPrice.get(i)) {
                n += 1;
            } else {
                break;
            }
        }
        return n;
    }
}
