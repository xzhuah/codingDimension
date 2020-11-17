package nodes.datascienceNode.stockInfo.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Xinyu Zhu on 2020/11/15, 0:43
 * nodes.datascienceNode.stockInfo.models in codingDimensionTemplate
 *
 * The standard way of calculating this feature is defined in @StockExpectedReturnFeature
 */
@Getter
@Setter
@NoArgsConstructor
public class StockReturnIndicator implements Comparable<StockReturnIndicator>{
    private double avgReturnDays;
    private double avgReturnProbability;
    private double stdReturnDays;

    @Override
    public String toString() {
        return String.format("(Prob=%.3f, Duration=%.2f, Std=%.2f)", avgReturnProbability, avgReturnDays, stdReturnDays);
    }

    @Override
    public int compareTo(StockReturnIndicator other) {
        // We might use a weighted schema to better compare
        if (avgReturnProbability > other.getAvgReturnProbability()) {
            return 1;
        } else if (avgReturnProbability < other.getAvgReturnProbability()) {
            return -1;
        } else {
            return Double.compare(other.getAvgReturnDays(), avgReturnDays);
        }
    }
}
