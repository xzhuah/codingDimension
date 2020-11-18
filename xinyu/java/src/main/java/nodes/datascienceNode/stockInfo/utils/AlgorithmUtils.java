package nodes.datascienceNode.stockInfo.utils;

import com.google.common.primitives.Ints;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Xinyu Zhu on 2020/11/15, 0:07
 * nodes.datascienceNode.stockInfo.utils in codingDimensionTemplate
 */
public class AlgorithmUtils {
    /**
     * 问题: 如果我采取如下策略进行交易: 在某日买入后, 等到价格达到或超过了我指定收益率后我既卖出, 那么我将有多大概率能达到我的目标? 一天内只能买或卖一次
     *
     * @param priceList  一个按时间先后排序的每日价格列表
     * @param returnRate 指定的目标收益率
     * @return
     */
    public static double returnProbability(List<Double> priceList, double returnRate) {
        if (priceList.size() <= 1) {
            return 0.0;
        }
        int[] expectedDays = returnExpectedDays(priceList, returnRate);
        int totalReturnedDay = 0;
        for (int days : expectedDays) {
            if (days != 0) {
                totalReturnedDay += 1;
            }
        }
        return (totalReturnedDay * 1.0) / (1.0 * priceList.size());
    }

    /**
     * 问题: 如果我采取如下策略进行交易: 在某日买入后, 等到价格达到或超过了我指定收益率后我既卖出, 那么我平均需要等待多少天才能达到目标 (忽略那些无法达到目标的买入天数, 一天内只能买或卖一次)
     *
     * @param priceList  一个按时间先后排序的每日价格列表
     * @param returnRate 指定的目标收益率
     * @return
     */
    public static double returnExpectedDay(List<Double> priceList, double returnRate) {
        if (priceList.size() <= 1) {
            return -1;
        }
        int[] expectedDays = returnExpectedDays(priceList, returnRate);
        int totalDayWaited = 0;
        int totalReturnedDay = 0;
        for (int days : expectedDays) {
            if (days != 0) {
                totalDayWaited += days;
                totalReturnedDay += 1;
            }
        }
        return (totalDayWaited * 1.0) / (totalReturnedDay * 1.0);
    }

    // This method is O(N^2), we found a O(NLogN) algorithm
    @Deprecated
    public static int[] returnExpectedDaysNaive(final List<Double> priceList, double returnRate) {
        int[] resultDays = new int[priceList.size()];
        for (int i = 0; i < priceList.size(); i++) {
            resultDays[i] = 0;
        }
        // O(N^2) algorithm
        for (int i = 0; i < priceList.size() - 1; i++) {
            double expectedPrice = priceList.get(i) * (1 + returnRate);
            for (int j = i + 1; j < priceList.size(); j++) {
                if (priceList.get(j) >= expectedPrice) {
                    resultDays[i] = j - i;
                    break;
                }
            }
        }
        return resultDays;
    }

    public static int[] returnExpectedDays(final List<Double> valueList, double returnRate) {
        returnRate += 1;
        int[] result = new int[valueList.size()];
        // ImmutablePair is compared on left first, then right
        PriorityQueue<ImmutablePair<Double, Integer>> minHeap = new PriorityQueue<>();

        // Initialize O(n)
        for (int i = 0; i < valueList.size(); i++) {
            result[i] = 0;
        }
        if (valueList.size() <= 1) return result;

        minHeap.add(ImmutablePair.of(valueList.get(0) * returnRate, 0));

        for (int i = 1; i <valueList.size(); i++) {
            double currentElement = valueList.get(i);
            while (!minHeap.isEmpty() && minHeap.peek().left <= currentElement) {
                ImmutablePair<Double, Integer> founded = minHeap.poll();
                result[founded.right] = i - founded.right;
            }
            minHeap.add(ImmutablePair.of(currentElement * returnRate, i));
        }
        return result;
    }


    public static void main(String[] args) {
        List<Double> input = List.of(2.0, 5.0, 4.0, 7.0, 3.0, 8.0, 9.0, 6.0);

        double returnRate = 1;
        int[] result1 = returnExpectedDaysNaive(input, returnRate);
        int[] result2 = returnExpectedDays(input, returnRate);

        System.out.println(Ints.asList(result1));
        System.out.println(Ints.asList(result2));
    }
}
