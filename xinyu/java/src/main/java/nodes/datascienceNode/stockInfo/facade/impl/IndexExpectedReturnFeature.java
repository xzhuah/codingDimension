package nodes.datascienceNode.stockInfo.facade.impl;

import com.google.common.math.Stats;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import nodes.datascienceNode.stockInfo.models.StockReturnIndicator;
import nodes.datascienceNode.stockInfo.utils.AlgorithmUtils;
import nodes.datascienceNode.stockInfo.utils.Converter;
import nodes.featureEngineeringNode.facade.ComparableFeature;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;
import java.util.stream.Collectors;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/18, 20:45
 * nodes.datascienceNode.stockInfo.facade.impl in codingDimensionTemplate
 *
 * Index is just a list of stock
 */
public class IndexExpectedReturnFeature implements ComparableFeature<List<List<StockDailyRecordPOJO>>, StockReturnIndicator> {

    private int sampleDays;
    private double returnRatio;

    public IndexExpectedReturnFeature(int sampleDays, double returnRatio) {
        this.sampleDays = sampleDays;
        this.returnRatio = returnRatio;
    }


    @Override
    public String getFeatureName() {
        return String.format("(ReturnRatio=%s, Duration=%d)", returnRatio, sampleDays);
    }

    @Override
    public StockReturnIndicator extractForInstance(List<List<StockDailyRecordPOJO>> target) {
        return null;
    }

    private static StockReturnIndicator getStockReturnIndicator(List<List<StockDailyRecordPOJO>> sortedDailyPrices, double returnRatio, int daysFromEnd) {
        checkStatus(sortedDailyPrices.size() > 0, "You can't get StockReturnIndicator with an empty StockDailyRecordPOJO List");
        int minArraySize = Integer.MAX_VALUE;
        for (List<StockDailyRecordPOJO> sortedDailyPrice : sortedDailyPrices) {
            if (sortedDailyPrice.size() < minArraySize) {
                minArraySize = sortedDailyPrice.size();
            }
        }
        daysFromEnd = Math.min(daysFromEnd, minArraySize);
        double[] indexAvgPrice = new double[daysFromEnd];
        for (int i = 0; i < daysFromEnd; i++) {
            indexAvgPrice[i] = 0;
        }
        for (List<StockDailyRecordPOJO> sortedDailyPrice : sortedDailyPrices) {
            sortedDailyPrice = sortedDailyPrice.subList(sortedDailyPrice.size() - daysFromEnd, sortedDailyPrice.size());
            List<Double> sortedDailyAvgPrice = Converter.toDailyAvgPriceList(sortedDailyPrice);
            for (int i = 0; i < daysFromEnd; i++) {
                indexAvgPrice[i] += sortedDailyAvgPrice.get(i);
            }
        }
        for (int i = 0; i < daysFromEnd; i++) {
            indexAvgPrice[i] /= sortedDailyPrices.size();
        }
        List<Double> sortedDailyAvgPrice = Doubles.asList(indexAvgPrice);

        int[] expectedDays = AlgorithmUtils.returnExpectedDays(sortedDailyAvgPrice, returnRatio);

        List<Integer> allReturnDays = Ints.asList(expectedDays).stream().filter(day -> day > 0).collect(Collectors.toList());
        Stats statistics = Stats.of(allReturnDays);

        StockReturnIndicator stockReturnIndicator = new StockReturnIndicator();
        stockReturnIndicator.setAvgReturnProbability((statistics.count() * 1.0) / (daysFromEnd * 1.0));
        if (statistics.count() == 0) {
            stockReturnIndicator.setAvgReturnDays(daysFromEnd);
            stockReturnIndicator.setStdReturnDays(0);
        } else {
            stockReturnIndicator.setAvgReturnDays(statistics.mean());
            stockReturnIndicator.setStdReturnDays(statistics.populationStandardDeviation());
        }
        return stockReturnIndicator;
    }
}
