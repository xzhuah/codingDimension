package nodes.datascienceNode.stockInfo.utils;

import java.util.List;

/**
 * Created by Xinyu Zhu on 2020/11/15, 0:07
 * nodes.datascienceNode.stockInfo.utils in codingDimensionTemplate
 */
public class AlgorithmUtils {
    /**
     * 问题: 如果我采取如下策略进行交易: 在某日买入后, 等到价格达到或超过了我指定收益率后我既卖出, 那么我将有多大概率能达到我的目标? 一天内只能买或卖一次
     * @param priceList 一个按时间先后排序的每日价格列表
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
     * @param priceList 一个按时间先后排序的每日价格列表
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

    public static int[] returnExpectedDays(List<Double> priceList, double returnRate) {
        int[] resultDays = new int[priceList.size()];
        for (int i = 0; i < priceList.size() ; i++) {
            resultDays[i] = 0;
        }
        // I can only think of this O(N^2) algorithm lol
        for (int i = 0; i <priceList.size() - 1; i++) {
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
}
