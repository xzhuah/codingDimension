package nodes.datascienceNode.stockInfo.facade.impl;

import lombok.Getter;
import lombok.Setter;
import nodes.datascienceNode.stockInfo.models.StockReturnIndicator;
import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/15, 0:54
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 */
@Setter
@Getter
public class StockExpectedReturnFeature implements ComparableFeature<List<StockDailyRecordPOJO>, StockReturnIndicator> {

    private int sampleDays;
    private double returnRatio;

    public StockExpectedReturnFeature(int sampleDays, double returnRatio) {
        this.sampleDays = sampleDays;
        this.returnRatio = returnRatio;
    }

    @Override
    public StockReturnIndicator extractForInstance(List<StockDailyRecordPOJO> target) {
        return StockReturnIndicator.getInstance(target, returnRatio, sampleDays);
    }

    @Override
    public String getFeatureName() {
        return String.format("(ReturnRatio=%s, Duration=%d)", returnRatio, sampleDays);
    }
}
