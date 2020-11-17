package nodes.datascienceNode.stockInfo.facade.impl;

import com.google.common.math.Stats;
import com.google.common.primitives.Ints;
import lombok.Getter;
import lombok.Setter;
import nodes.datascienceNode.stockInfo.models.StockReturnIndicator;
import nodes.datascienceNode.stockInfo.utils.AlgorithmUtils;
import nodes.datascienceNode.stockInfo.utils.Converter;
import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;
import java.util.stream.Collectors;

import static common.utils.ConditionChecker.checkStatus;

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
        // Just make use of the algorithm defined in StockReturnIndicator
        return getStockReturnIndicator(target, returnRatio, sampleDays);
    }

    @Override
    public String getFeatureName() {
        return String.format("(ReturnRatio=%s, Duration=%d)", returnRatio, sampleDays);
    }


    private static StockReturnIndicator getStockReturnIndicator(List<StockDailyRecordPOJO> sortedDailyPrice, double returnRatio, int daysFromEnd) {
        checkStatus(sortedDailyPrice.size() > 0, "You can't get StockReturnIndicator with an empty StockDailyRecordPOJO List");
        if (sortedDailyPrice.size() > daysFromEnd) {
            sortedDailyPrice = sortedDailyPrice.subList(sortedDailyPrice.size() - daysFromEnd, sortedDailyPrice.size());
        }
        StockReturnIndicator stockReturnIndicator = new StockReturnIndicator();
        List<Double> sortedDailyAvgPrice = Converter.toDailyAvgPriceList(sortedDailyPrice);
        int[] expectedDays = AlgorithmUtils.returnExpectedDays(sortedDailyAvgPrice, returnRatio);


        List<Integer> allReturnDays = Ints.asList(expectedDays).stream().filter(day -> day > 0).collect(Collectors.toList());
        Stats statistics = Stats.of(allReturnDays);

        stockReturnIndicator.setAvgReturnProbability((statistics.count() * 1.0) / (sortedDailyPrice.size() * 1.0));
        if (statistics.count() == 0) {
            stockReturnIndicator.setAvgReturnDays(sortedDailyPrice.size());
            stockReturnIndicator.setStdReturnDays(0);
        } else {
            stockReturnIndicator.setAvgReturnDays(statistics.mean());
            stockReturnIndicator.setStdReturnDays(statistics.populationStandardDeviation());
        }
        return stockReturnIndicator;
    }
}
