package nodes.datascienceNode.stockInfo.impl;

import com.google.inject.Inject;
import common.time.TimeInterval;
import nodes.NodeModule;
import nodes.datascienceNode.stockInfo.StockFilterService;
import nodes.datascienceNode.stockInfo.facade.impl.DaysOverFeature;
import nodes.datascienceNode.stockInfo.facade.impl.StockNdayAvgPriceSlopeFeature;
import nodes.stockinfoNode.StockInfoService;
import nodes.stockinfoNode.models.StockDailyRecordPOJO;

import java.util.*;

/**
 * Created by Xinyu Zhu on 2020/11/29, 0:44
 * nodes.datascienceNode.stockInfo.impl in codingDimensionTemplate
 * <p>
 * 用于寻找满足特定要求的股票
 */
public class StockFilterServiceImpl implements StockFilterService {

    private final StockInfoService stockInfoService;

    @Inject
    public StockFilterServiceImpl(StockInfoService stockInfoService) {
        this.stockInfoService = stockInfoService;
    }

    // 寻找具备5日均线向上穿过10日均线形成金叉趋势的股票
    // 1. 5日均线在10日均线下方至少n日 -->
    // 2. 最近m日5日均线斜率大于0且大于10日均线斜率
    public Map<String, Double> filterGoldenCrossStock(int n, int m) {
        DaysOverFeature daysOverFeature = new DaysOverFeature(5, 10);
        StockNdayAvgPriceSlopeFeature fiveDaySlope = new StockNdayAvgPriceSlopeFeature(5);
        StockNdayAvgPriceSlopeFeature tenDaySlope = new StockNdayAvgPriceSlopeFeature(10);

        List<String> allSymbols = stockInfoService.getAllSymbols();
        Map<String, Double> goldenCrossStock = new HashMap<>();

        long duration = (Math.max(n, m) + 11L) * 2L * 24L * 3600L * 1000L;
        for (String symbol : allSymbols) {
            List<StockDailyRecordPOJO> sortedPrice = stockInfoService.getSortedPriceForSymbol(symbol, TimeInterval.getUpToNowIntervalWithDuration(duration)).getAllModel(StockDailyRecordPOJO.class).get();
            if (sortedPrice.isEmpty()) {
                continue;
            }
            int daysOver = daysOverFeature.extractForInstance(sortedPrice);
            if (daysOver < n) {
                continue;
            }
            List<Double> fiveDaySlopeData = fiveDaySlope.extractForInstance(sortedPrice);

            boolean fiveDaySlopeWithMDayIsPositive = true;
            for (int i = fiveDaySlopeData.size() - 1; i >= fiveDaySlopeData.size() - m; i--) {
                if (fiveDaySlopeData.get(i) <= 0) {
                    fiveDaySlopeWithMDayIsPositive = false;
                    break;
                }
            }
            if (!fiveDaySlopeWithMDayIsPositive) {
                continue;
            }

            boolean fiveDaySlopeWithMDayIsLargerThenTenDays = true;
            List<Double> tenDaySlopeData = tenDaySlope.extractForInstance(sortedPrice);

            for (int i = fiveDaySlopeData.size() - 1; i >= fiveDaySlopeData.size() - m; i--) {
                if (fiveDaySlopeData.get(i) <= tenDaySlopeData.get(i)) {
                    fiveDaySlopeWithMDayIsLargerThenTenDays = false;
                    break;
                }
            }

            if (!fiveDaySlopeWithMDayIsLargerThenTenDays) {
                continue;
            }

            double slopeAvg = 0;
            for (Double slope : fiveDaySlopeData) {
                slopeAvg += slope;
            }
            slopeAvg /= fiveDaySlopeData.size();
            goldenCrossStock.put(symbol, slopeAvg);

        }

        return goldenCrossStock;

    }

    public static void main(String[] args) {
        StockFilterService stockFilterService = NodeModule.getGlobalInjector().getInstance(StockFilterService.class);
        Map<String, Double> symbols = stockFilterService.filterGoldenCrossStock(3, 2);

        System.out.println(sortByValue(symbols));
    }

    // function to sort hashmap by values
    public static HashMap<String, Double> sortByValue(Map<String, Double> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list =
                new LinkedList<Map.Entry<String, Double> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2)
            {
                return -(o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
