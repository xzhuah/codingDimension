package nodes.datascienceNode.stockInfo.models;


import com.google.common.primitives.Ints;
import lombok.Getter;
import nodes.datascienceNode.stockInfo.utils.AlgorithmUtils;
import nodes.datascienceNode.stockInfo.utils.Converter;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.List;

import static common.utils.ConditionChecker.checkStatus;

/**
 * Created by Xinyu Zhu on 2020/11/15, 0:43
 * nodes.datascienceNode.stockInfo.models in codingDimensionTemplate
 */
@Getter
public class StockReturnIndicator implements Comparable<StockReturnIndicator>{
    private String symbol;
    private double returnRatio;
    private int sampleDays;
    private long endTime;

    // these are calculated
    private List<Integer> returnDays;
    private double avgReturnDays;
    private double avgReturnProbability;

    private StockReturnIndicator() {

    }

    @Override
    public String toString() {
        return String.format("(Prob=%.3f, Duration=%.2f)", avgReturnProbability, avgReturnDays);
    }

    public static StockReturnIndicator getInstance(List<StockDailyRecordPOJO> sortedDailyPrice, double returnRatio, int daysFromEnd) {
        checkStatus(sortedDailyPrice.size() > 0, "You can't get StockReturnIndicator with an empty StockDailyRecordPOJO List");
        if (sortedDailyPrice.size() > daysFromEnd) {
            sortedDailyPrice = sortedDailyPrice.subList(sortedDailyPrice.size() - daysFromEnd, sortedDailyPrice.size());
        }
        StockReturnIndicator stockReturnIndicator = new StockReturnIndicator();
        stockReturnIndicator.returnRatio = returnRatio;
        stockReturnIndicator.symbol = sortedDailyPrice.get(0).getSymbol();
        stockReturnIndicator.sampleDays = sortedDailyPrice.size();
        stockReturnIndicator.endTime = sortedDailyPrice.get(sortedDailyPrice.size()-1).getTime();

        List<Double> sortedDailyAvgPrice = Converter.toDailyAvgPriceList(sortedDailyPrice);
        int[] expectedDays = AlgorithmUtils.returnExpectedDays(sortedDailyAvgPrice, returnRatio);
        stockReturnIndicator.returnDays = Ints.asList(expectedDays);

        int totalDayWaited = 0;
        int totalReturnedDay = 0;
        for (int days : expectedDays) {
            if (days != 0) {
                totalDayWaited += days;
                totalReturnedDay += 1;
            }
        }
        stockReturnIndicator.avgReturnProbability = (totalReturnedDay * 1.0) / (stockReturnIndicator.sampleDays * 1.0);
        if (totalReturnedDay == 0) {
            stockReturnIndicator.avgReturnDays = stockReturnIndicator.sampleDays;
        } else {
            stockReturnIndicator.avgReturnDays = (totalDayWaited * 1.0) / (totalReturnedDay * 1.0);
        }
        return stockReturnIndicator;
    }

    public static StockReturnIndicator getInstance(List<StockDailyRecordPOJO> sortedDailyPrice, double returnRatio) {
        return getInstance(sortedDailyPrice, returnRatio, sortedDailyPrice.size());
    }

    public static StockReturnIndicator getInstanceWithSorting(List<StockDailyRecordPOJO> sortedDailyPrice, double returnRatio) {
        return getInstanceWithSorting(sortedDailyPrice, returnRatio, sortedDailyPrice.size());
    }

    public static StockReturnIndicator getInstanceWithSorting(List<StockDailyRecordPOJO> sortedDailyPrice, double returnRatio, int daysFromEnd) {
        sortedDailyPrice.sort((record1, record2) -> {
            long timeDiff = record1.getTime() - record2.getTime();
            if (timeDiff > 0) {
                return 1;
            } else if (timeDiff < 0) {
                return -1;
            } else {
                return 0;
            }
        });
        return getInstance(sortedDailyPrice, returnRatio, daysFromEnd);
    }

    @Override
    public int compareTo(StockReturnIndicator other) {
        if (avgReturnProbability > other.getAvgReturnProbability()) {
            return 1;
        } else if (avgReturnProbability < other.getAvgReturnProbability()) {
            return -1;
        } else {
            return Double.compare(other.getAvgReturnDays(), avgReturnDays);
        }
    }
}
